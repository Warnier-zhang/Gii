package org.warnier.zhang.gii.generator.def;

import java.sql.Types;

/**
 * 维护MySQL数据类型与Java数据类型间的转换关系。
 */
public class MysqlDefs {
    /**
     * 把MySQL数据类型转换成JDBC数据类型。
     *
     * @param columnType MySQL数据类型；
     * @return
     */
    public static int toJdbcType(String columnType, int columnScale) {
        int jdbcType;
        switch (columnType.toUpperCase()) {
            case "BIT":
                jdbcType = Types.BIT;
                break;
            case "TINYINT":
                jdbcType = Types.TINYINT;
                break;
            case "SMALLINT":
                jdbcType = Types.SMALLINT;
                break;
            case "MEDIUMINT":
                jdbcType = Types.INTEGER;
                break;
            case "INT":
                jdbcType = Types.INTEGER;
                break;
            case "INTEGER":
                jdbcType = Types.INTEGER;
                break;
            case "BIGINT":
                jdbcType = Types.BIGINT;
                break;
            case "INT24":
                jdbcType = Types.INTEGER;
                break;
            case "REAL":
                jdbcType = Types.DOUBLE;
                break;
            case "FLOAT":
                jdbcType = Types.REAL;
                break;
            case "DECIMAL":
                jdbcType = Types.DECIMAL;
                break;
            case "NUMERIC":
                jdbcType = Types.DECIMAL;
                break;
            case "DOUBLE":
                jdbcType = Types.DOUBLE;
                break;
            case "CHAR":
                jdbcType = Types.CHAR;
                break;
            case "VARCHAR":
                jdbcType = Types.VARCHAR;
                break;
            case "DATE":
                jdbcType = Types.DATE;
                break;
            case "TIME":
                jdbcType = Types.TIME;
                break;
            case "YEAR":
                jdbcType = Types.DATE;
                break;
            case "TIMESTAMP":
                jdbcType = Types.TIMESTAMP;
                break;
            case "DATETIME":
                jdbcType = Types.TIMESTAMP;
                break;
            case "TINYBLOB":
                jdbcType = Types.BINARY;
                break;
            case "BLOB":
                jdbcType = Types.LONGVARBINARY;
                break;
            case "MEDIUMBLOB":
                jdbcType = Types.LONGVARBINARY;
                break;
            case "LONGBLOB":
                jdbcType = Types.LONGVARBINARY;
                break;
            case "TINYTEXT":
                jdbcType = Types.VARCHAR;
                break;
            case "TEXT":
                jdbcType = Types.LONGVARCHAR;
                break;
            case "MEDIUMTEXT":
                jdbcType = Types.LONGVARCHAR;
                break;
            case "LONGTEXT":
                jdbcType = Types.LONGVARCHAR;
                break;
            case "ENUM":
                jdbcType = Types.CHAR;
                break;
            case "SET":
                jdbcType = Types.CHAR;
                break;
            case "GEOMETRY":
                jdbcType = Types.BINARY;
                break;
            case "BINARY":
                jdbcType = Types.BINARY;
                break;
            case "VARBINARY":
                jdbcType = Types.VARBINARY;
                break;
            default:
                jdbcType = Types.OTHER;
        }
        return jdbcType;
    }
}
