package org.warnier.zhang.gii.generator;

import org.warnier.zhang.gii.generator.def.MysqlDefs;
import org.warnier.zhang.gii.generator.def.OracleDefs;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换器。
 */
public class TypeResolver {
    /**
     * JAVA类型和JDBC类型映射数据集合；
     */
    private Map<Integer, String[]> typeMappers;

    /**
     * TypeResolver类实例；
     */
    private static TypeResolver typeResolver;

    private TypeResolver() {
        typeMappers = new HashMap<>();
        typeMappers.put(Types.ARRAY, new String[]{"ARRAY", Object.class.getSimpleName()});
        typeMappers.put(Types.BIGINT, new String[]{"BIGINT", Long.class.getSimpleName()});
        typeMappers.put(Types.BINARY, new String[]{"BINARY", "byte[]"});
        typeMappers.put(Types.BIT, new String[]{"BIT", Boolean.class.getSimpleName()});
        typeMappers.put(Types.BLOB, new String[]{"BLOB", "byte[]"});
        typeMappers.put(Types.BOOLEAN, new String[]{"BOOLEAN", Boolean.class.getSimpleName()});
        typeMappers.put(Types.CHAR, new String[]{"CHAR", String.class.getSimpleName()});
        typeMappers.put(Types.CLOB, new String[]{"CLOB", String.class.getSimpleName()});
        typeMappers.put(Types.DATALINK, new String[]{"DATALINK", Object.class.getSimpleName()});
        typeMappers.put(Types.DATE, new String[]{"DATE", Date.class.getSimpleName()});
        typeMappers.put(Types.DECIMAL, new String[]{"DECIMAL", BigDecimal.class.getSimpleName()});
        typeMappers.put(Types.DISTINCT, new String[]{"DISTINCT", Object.class.getSimpleName()});
        typeMappers.put(Types.DOUBLE, new String[]{"DOUBLE", Double.class.getSimpleName()});
        typeMappers.put(Types.FLOAT, new String[]{"FLOAT", Double.class.getSimpleName()});
        typeMappers.put(Types.INTEGER, new String[]{"INTEGER", Integer.class.getSimpleName()});
        typeMappers.put(Types.JAVA_OBJECT, new String[]{"JAVA_OBJECT", Object.class.getSimpleName()});
        typeMappers.put(Types.LONGNVARCHAR, new String[]{"LONGNVARCHAR", String.class.getSimpleName()});
        typeMappers.put(Types.LONGVARBINARY, new String[]{"LONGVARBINARY", "byte[]"});
        typeMappers.put(Types.LONGVARCHAR, new String[]{"LONGVARCHAR", String.class.getSimpleName()});
        typeMappers.put(Types.NCHAR, new String[]{"NCHAR", String.class.getSimpleName()});
        typeMappers.put(Types.NCLOB, new String[]{"NCLOB", String.class.getSimpleName()});
        typeMappers.put(Types.NVARCHAR, new String[]{"NVARCHAR", String.class.getSimpleName()});
        typeMappers.put(Types.NULL, new String[]{"NULL", Object.class.getSimpleName()});
        typeMappers.put(Types.NUMERIC, new String[]{"NUMERIC", BigDecimal.class.getSimpleName()});
        typeMappers.put(Types.OTHER, new String[]{"OTHER", Object.class.getSimpleName()});
        typeMappers.put(Types.REAL, new String[]{"REAL", Float.class.getSimpleName()});
        typeMappers.put(Types.REF, new String[]{"REF", Object.class.getSimpleName()});
        typeMappers.put(Types.SMALLINT, new String[]{"SMALLINT", Short.class.getSimpleName()});
        typeMappers.put(Types.STRUCT, new String[]{"STRUCT", Object.class.getSimpleName()});
        typeMappers.put(Types.TIME, new String[]{"TIME", Date.class.getSimpleName()});
        typeMappers.put(Types.TIMESTAMP, new String[]{"TIMESTAMP", Date.class.getSimpleName()});
        typeMappers.put(Types.TINYINT, new String[]{"TINYINT", Byte.class.getSimpleName()});
        typeMappers.put(Types.VARBINARY, new String[]{"VARBINARY", "byte[]"});
        typeMappers.put(Types.VARCHAR, new String[]{"VARCHAR", String.class.getSimpleName()});
    }

    /**
     * 解析JAVA类型。
     *
     * @param databaseId  数据库提供商；
     * @param columnType  类型名称；
     * @param columnScale 小数位数；
     * @return
     */
    public String parseJavaType(String databaseId, String columnType, int columnScale) {
        String javaType = "Object";

        int type = getColumnType(databaseId, columnType, columnScale);
        for (Map.Entry<Integer, String[]> typeMapper : typeMappers.entrySet()) {
            if (typeMapper.getKey() == type) {
                javaType = typeMapper.getValue()[1];
            }
        }
        return javaType;
    }

    /**
     * 解析JDBC类型。
     *
     * @param databaseId  数据库提供商；
     * @param columnType  类型名称；
     * @param columnScale 小数位数；
     * @return
     */
    public String parseJdbcType(String databaseId, String columnType, int columnScale) {
        String jdbcType = "VARCHAR";

        int type = getColumnType(databaseId, columnType, columnScale);
        for (Map.Entry<Integer, String[]> typeMapper : typeMappers.entrySet()) {
            if (typeMapper.getKey() == type) {
                jdbcType = typeMapper.getValue()[0];
            }
        }
        return jdbcType;
    }

    /**
     * 把类型名称转换成类型的值。
     *
     * @param databaseId  数据库提供商；
     * @param columnType  类型名称；
     * @param columnScale 小数位数；
     * @return 字段类型，是java.sql.Types的枚举值
     */
    private int getColumnType(String databaseId, String columnType, int columnScale) {
        int jdbcType = Types.OTHER;
        if (databaseId.equals("mysql")) {
            jdbcType = MysqlDefs.toJdbcType(columnType, columnScale);
        } else if (databaseId.equals("oracle")) {
            jdbcType = OracleDefs.toJdbcType(columnType, columnScale);
        }
        return jdbcType;
    }

    /**
     * TypeResolver类工厂方法。
     *
     * @return
     */
    public static TypeResolver getTypeResolver() {
        if (typeResolver == null) {
            typeResolver = new TypeResolver();
        }
        return typeResolver;
    }
}
