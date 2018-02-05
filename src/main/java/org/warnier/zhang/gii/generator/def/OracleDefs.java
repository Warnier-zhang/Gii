package org.warnier.zhang.gii.generator.def;

import java.lang.reflect.Type;
import java.sql.Types;

/**
 * 维护Oracle数据类型与Java数据类型间的转换关系。
 */
public class OracleDefs {
    /**
     * 把Oracle数据类型转换成JDBC数据类型。
     *
     * @param columnType Oracle数据类型；
     * @return
     */
    public static int toJdbcType(String columnType, int columnScale) {
        int jdbcType;
        switch (columnType.toUpperCase()) {
            case "CHAR":
                jdbcType = Types.CHAR;
                break;
            case "VARCHAR2":
                jdbcType = Types.VARCHAR;
                break;
            case "LONG":
                jdbcType = Types.LONGVARCHAR;
                break;
            case "NUMBER":
                if (columnScale == -1) {
                    jdbcType = Types.DOUBLE;
                } else if (columnScale == 0) {
                    jdbcType = Types.BIGINT;
                } else {
                    jdbcType = Types.DOUBLE;
                }
                break;
            case "RAW":
                jdbcType = Types.BINARY;
                break;
            case "LONGRAW":
                jdbcType = Types.LONGVARBINARY;
                break;
            case "DATE":
                jdbcType = Types.DATE;
                break;
            case "TIMESTAMP":
                jdbcType = Types.TIMESTAMP;
                break;
            case "BLOB":
                jdbcType = Types.BLOB;
                break;
            case "CLOB":
                jdbcType = Types.CLOB;
                break;
            case "ROWID":
                jdbcType = Types.ROWID;
                break;
            case "NCLOB":
                jdbcType = Types.NCLOB;
                break;
            case "NCHAR":
                jdbcType = Types.NCHAR;
                break;
            default:
                jdbcType = Types.OTHER;
        }
        return jdbcType;
    }
}
