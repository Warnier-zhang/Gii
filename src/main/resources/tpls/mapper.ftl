package ${packageName};

import ${domainPackageName}.${className?substring(0, className?index_of("Mapper"))};

/**
 * 表“${tableName}” 的MAPPER接口。
 */
public interface ${className} extends Mapper<${className?substring(0, className?index_of("Mapper"))}> {

}