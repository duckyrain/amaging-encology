package cn.amaging.encology.es.common;

/**
 * Created by DuQiyu on 2018/6/19.
 */
public class SearchConstants {

    /************************业务类型TYPE*******************************/
    public static final String USER = "user";

    // 索引
    public static final String INDEX = "index";
    // 类型（注意：不是es自带的_type，为source里的自定义type）
    public static final String TYPE = "type";
    // 文档唯一id
    public static final String UID = "uid";
    // 查询条件
    public static final String COND = "cond";
    public static final String MAC = "mac";
    // 分页
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String PAGE_SIZE = "pageSize";
    // 排序
    public static final String SORT = "sort";
    public static final String SORT_DEFAULT_FIELD = "-update_time";
    public static final char SORT_ASC = '+';
    public static final char SORT_DESC = '-';
    // 非
    public static final char NOT = '!';
    // 范围查询
    public static final String BY = "by";
    public static final String GTE = "gte";
    public static final String LTE = "lte";
    // 自定义域
    public static final String FIELDS = "fields";
    // 分隔符
    public static final String SPLIT_COMMA = ",";
    public static final String SPLIT_SEMICOLON = ";";

}
