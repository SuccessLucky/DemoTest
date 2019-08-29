package com.project.common;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by xieyanhao on 16/3/13.
 */
public interface RowMapper
{
    public Object mapRow(ResultSet rs, int index)
            throws SQLException;
}
