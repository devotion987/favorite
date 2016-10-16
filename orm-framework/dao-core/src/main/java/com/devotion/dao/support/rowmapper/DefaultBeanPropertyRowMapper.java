package com.devotion.dao.support.rowmapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.devotion.dao.DataBaseOperation;

/**
 * 翻页处理规则 原来的find方法ORMapping没有依赖Column注解里的配置,是采用Spring JDBC
 * Template默认的Mapping机制，导致java属性与数据表字段不匹配的情况发生 现通过自定义的rowmapper来实现
 *
 * @param <T> 泛型T
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DefaultBeanPropertyRowMapper<T> implements RowMapper<T> {

    /**
     * Logger available to subclasses
     */
    private final Logger logger = LoggerFactory.getLogger(DataBaseOperation.class);
    /**
     * The class we are mapping to
     */
    private Class<T> mappedClass;

    /**
     * Whether we're strictly validating
     */
    private boolean checkFullyPopulated;

    /**
     * Whether we're defaulting primitives when mapping a null value
     */
    private boolean primitivesDefaultedForNullValue;

    /**
     * Map of the fields we provide mapping for
     */
    private Map<String, PropertyDescriptor> mappedFields;

    /**
     * Set of bean properties we provide mapping for
     */
    private Set<String> mappedProperties;

    /**
     * Create a new BeanPropertyRowMapper for bean-style configuration.
     *
     * @see #setMappedClass
     * @see #setCheckFullyPopulated
     */
    public DefaultBeanPropertyRowMapper() {
    }

    /**
     * Create a new BeanPropertyRowMapper, accepting unpopulated properties in
     * the target bean.
     * <p>
     * Consider using the {@link #newInstance} factory method instead, which
     * allows for specifying the mapped type once only.
     *
     * @param mappedClass the class that each row should be mapped to
     */
    public DefaultBeanPropertyRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    /**
     * Create a new BeanPropertyRowMapper.
     *
     * @param mappedClass         the class that each row should be mapped to
     * @param checkFullyPopulated whether we're strictly validating that all bean properties
     *                            have been mapped from corresponding database fields
     */
    public DefaultBeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Set the class that each row should be mapped to.
     *
     * @param mappedClass the mapped class.
     */
    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            initialize(mappedClass);
        } else {
            if (!this.mappedClass.equals(mappedClass)) {
                throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to "
                        + mappedClass + " since it is already providing mapping for " + this.mappedClass);
            }
        }
    }

    /**
     * Initialize the mapping metadata for the given class.
     *
     * @param mappedClass the mapped class.
     */
    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<>();
        this.mappedProperties = new HashSet<>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getReadMethod() != null) {
                Column column = pd.getReadMethod().getAnnotation(Column.class);
                if (column != null) {
                    this.mappedFields.put(column.name().toLowerCase(), pd);
                    this.mappedProperties.add(pd.getName());
                } else {
                    this.mappedFields.put(pd.getName().toLowerCase(), pd);
                    String underscoredName = underscoreName(pd.getName());
                    if (!pd.getName().toLowerCase().equals(underscoredName)) {
                        this.mappedFields.put(underscoredName, pd);
                    }
                    this.mappedProperties.add(pd.getName());
                }
            }
        }
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case. Any
     * upper case letters are converted to lower case with a preceding
     * underscore.
     *
     * @param name the string containing original name
     * @return the converted name
     */
    private String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    /**
     * Get the class that we are mapping to.
     *
     * @return class 映射类
     */
    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    /**
     * Set whether we're strictly validating that all bean properties have been
     * mapped from corresponding database fields.
     * <p>
     * Default is {@code false}, accepting unpopulated properties in the target
     * bean.
     *
     * @param checkFullyPopulated Return whether we're strictly validating that all bean
     *                            properties have been mapped from corresponding database
     *                            fields.
     */
    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Return whether we're strictly validating that all bean properties have
     * been mapped from corresponding database fields.
     *
     * @return 判断结果
     */
    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    /**
     * Set whether we're defaulting Java primitives in the case of mapping a
     * null value from corresponding database fields.
     * <p>
     * Default is {@code false}, throwing an exception when nulls are mapped to
     * Java primitives.
     *
     * @param primitivesDefaultedForNullValue Return whether we're defaulting Java primitives in the case of
     *                                        mapping a null value from corresponding database fields.
     */
    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    /**
     * Return whether we're defaulting Java primitives in the case of mapping a
     * null value from corresponding database fields.
     *
     * @return 判断结果
     */
    public boolean isPrimitivesDefaultedForNullValue() {
        return primitivesDefaultedForNullValue;
    }

    /**
     * Extract the values for all columns in the current row.
     * <p>
     * Utilizes public setters and result set metadata.
     *
     * @param rs        结果集
     * @param rowNumber 行数
     * @return 结果值
     * @throws SQLException SQL异常
     * @see ResultSetMetaData
     */
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        initBeanWrapper(bw);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<>() : null);

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            PropertyDescriptor pd = this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, pd);
                    if (logger.isDebugEnabled() && rowNumber == 0) {
                        logger.debug("Mapping column '" + column + "' to property '" + pd.getName() + "' of type "
                                + pd.getPropertyType());
                    }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException e) {
                        if (value == null && primitivesDefaultedForNullValue) {
                            logger.debug("Intercepted TypeMismatchException for row " + rowNumber + " and column '"
                                    + column + "' with value null" + " when setting property '" + pd.getName()
                                    + "' of type " + pd.getPropertyType() + " on object: " + mappedObject);
                        } else {
                            throw e;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException(
                            "Unable to map column " + column + " to property " + pd.getName(), ex);
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields "
                    + "necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);
        }

        return mappedObject;
    }

    /**
     * Initialize the given BeanWrapper to be used for row mapping. To be called
     * for each row.
     * <p>
     * The default implementation is empty. Can be overridden in subclasses.
     *
     * @param bw the BeanWrapper to initialize
     */
    protected void initBeanWrapper(BeanWrapper bw) {
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>
     * The default implementation calls
     * {@link JdbcUtils#getResultSetValue(ResultSet, int, Class)}.
     * Subclasses may override this to check specific value types upfront, or to
     * post-process values return from {@code getResultSetValue}.
     *
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @param pd    the bean property that each result object is expected to match
     *              (or {@code null} if none specified)
     * @return the Object value
     * @throws SQLException in case of extraction failure
     */
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    /**
     * Static factory method to create a new BeanPropertyRowMapper (with the
     * mapped class specified only once).
     *
     * @param mappedClass the class that each row should be mapped to
     * @param <T>         泛型对象
     * @return newInstances
     */
    public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        BeanPropertyRowMapper<T> newInstance = new BeanPropertyRowMapper<>();
        newInstance.setMappedClass(mappedClass);
        return newInstance;
    }

}
