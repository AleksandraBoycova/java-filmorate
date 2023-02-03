package ru.yandex.practicum.filmorate.util;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPARowMapper implements RowMapper<MPA> {
    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(rs.getLong("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        mpa.setDescription(rs.getString("MPA.DESCRIPTION"));
        return mpa;
    }
}
