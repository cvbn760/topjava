package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = false, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            deleteRoles(user);
            insertRoles(user);
        }

        return user;
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = false, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = true, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = true, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = true, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public List<User> getAll() {
       // return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> map = new ConcurrentHashMap<>();
        jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
                       map.computeIfAbsent(rs.getInt("user_id"),
                               userId -> EnumSet.noneOf(Role.class))
                               .add(Role.valueOf(rs.getString("role")));
        });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.forEach(u -> u.setRoles(map.get(u.getId())));
        return users;
    }

    private void deleteRoles(User user){
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }

    private void insertRoles(User user){
        Set<Role> roleSet = user.getRoles();
        if (!roleSet.isEmpty()){
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roleSet, roleSet.size(),
                    (ps, role) -> {
                        ps.setInt(1, user.getId());
                        ps.setString(2, role.name());
                    });
        }
    }

    private User setRoles(User user){
        if (user != null){
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, user.getId());
            user.setRoles(roles);
        }
        return user;
    }
}
