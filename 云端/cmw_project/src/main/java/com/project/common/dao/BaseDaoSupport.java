package com.project.common.dao;

import com.project.common.PageResults;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseDaoSupport<T, ID extends Serializable> implements IBaseDao<T, ID> {

	@Autowired
    private SessionFactory sessionFactory;

	protected Class<T> entityClass;

	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return entityClass;
	}

	/**
	 * <保存实体>
	 * <完整保存实体>
	 * @param t 实体参数
	 */
	@Override
	public void save(T t) {
		this.getSession().save(t);
	}

	/**
	 * <保存或者更新实体>
	 * @param t 实体
	 */
	@Override
	public void saveOrUpdate(T t) {
		this.getSession().saveOrUpdate(t);
	}

	/**
	 * <load>
	 * <加载实体的load方法>
	 * @param id 实体的id
	 * @return 查询出来的实体
	 */
	@Override
	public T load(ID id) {
		T load = (T) this.getSession().load(getEntityClass(), id);
		return load;
	}

	/**
	 * <get>
	 * <查找的get方法>
	 * @param id 实体的id
	 * @return 查询出来的实体
	 */
	@Override
	public T get(ID id) {
		T load = (T) this.getSession().get(getEntityClass(), id);
		return load;
	}

	/**
	 * <contains>
	 * @param t 实体
	 * @return 是否包含
	 */
	@Override
	public boolean contains(T t) {
		return this.getSession().contains(t);
	}

	/**
	 * <delete>
	 * <删除表中的t数据>
	 * @param t 实体
	 */
	@Override
	public void delete(T t) {
		this.getSession().delete(t);
	}

	/**
	 * <根据ID删除数据>
	 * @param Id 实体id
	 * @return 是否删除成功
	 */
	@Override
	public boolean deleteById(ID Id) {
		T t = get(Id);
		if(t == null){
			return false;
		}
		delete(t);
		return true;
	}

	/**
	 * <删除所有>
	 * @param entities 实体的Collection集合
	 */
	@Override
	public void deleteAll(Collection<T> entities) {
		for(Object entity : entities) {
			this.getSession().delete(entity);
		}
	}

	/**
	 * <执行Hql语句>
	 * @param hqlString hql
	 * @param values 不定参数数组
	 */
	@Override
	public void queryHql(String hqlString, Object... values) {
		Query query = this.getSession().createQuery(hqlString);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		query.executeUpdate();
	}

	/**
	 * <执行Sql语句>
	 * @param sqlString sql
	 * @param values 不定参数数组
	 */
	@Override
	public void querySql(String sqlString, Object... values) {
		Query query = this.getSession().createSQLQuery(sqlString);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		query.executeUpdate();
	}

	/**
	 * <根据HQL语句查找唯一实体>
	 * @param hqlString HQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询实体
	 */
	@Override
	public T getByHQL(String hqlString, Object... values) {
		Query query = this.getSession().createQuery(hqlString);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		return (T) query.uniqueResult();
	}

	/**
	 * <根据SQL语句查找唯一实体>
	 * @param sqlString SQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询实体
	 */
	@Override
	public T getBySQL(String sqlString, Object... values) {
		Query query = this.getSession().createSQLQuery(sqlString);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		return (T) query.uniqueResult();
	}

	/**
	 * <根据HQL语句，得到对应的list>
	 * @param hqlString HQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	@Override
	public List<T> getListByHQL(String hqlString, Object... values) {
		Query query = this.getSession().createQuery(hqlString);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}

	/**
	 * <根据SQL语句，得到对应的list>
	 * @param sqlString HQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	@Override
	public List<T> getListBySQL(String sqlString, Object... values ) {
		Query query = this.getSession().createSQLQuery(sqlString);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}

	/**
	 * 由sql语句得到List
	 * @param sql
	 * @param map
	 * @param values
	 * @return List
	 */
	@Override
	public List findListBySql(final String sql, final com.project.common.RowMapper map, final Object... values) {
		final List list = new ArrayList();
		// 执行JDBC的数据批量保存
		Work jdbcWork = new Work()
		{
			public void execute(Connection connection)
					throws SQLException
			{

				PreparedStatement ps = null;
				ResultSet rs = null;
				try
				{
					ps = connection.prepareStatement(sql);
					for (int i = 0; i < values.length; i++)
					{
						setParameter(ps, i, values[i]);

					}

					rs = ps.executeQuery();
					int index = 0;
					while (rs.next())
					{
						Object obj = map.mapRow(rs, index++);
						list.add(obj);

					}
				}
				finally
				{
					if (rs != null)
					{
						rs.close();

					}
					if (ps != null)
					{
						ps.close();
					}
				}
			}
		};
		this.getSession().doWork(jdbcWork);
		return list;
	}

	/**
	 * <refresh>
	 * @param t 实体
	 */
	@Override
	public void refresh(T t) {
		this.getSession().refresh(t);
	}

	/**
	 * <update>
	 * @param t 实体
	 */
	@Override
	public void update(T t) {
		this.getSession().update(t);
	}

	/**
	 * <根据HQL得到记录数>
	 * @param hql HQL语句
	 * @param values 不定参数的Object数组
	 * @return 记录总数
	 */
	@Override
	public Long countByHql(String hql, Object... values) {
		Query query = this.getSession().createQuery(hql);
		if(values != null){
			for(int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return (Long) query.uniqueResult();
	}

	/**
	 * <HQL分页查询>
	 * @param hql HQL语句
	 * @param countHql 查询记录条数的HQL语句
	 * @param pageNo 下一页
	 * @param pageSize 一页总条数
	 * @param values 不定Object数组参数
	 * @return PageResults的封装类，里面包含了页码的信息以及查询的数据List集合
	 */
	@Override
	public PageResults<T> findPageByFetchedHql(String hql, String countHql,
											   int pageNo, int pageSize, Object... values) {
		PageResults<T> retValue = new PageResults<T>();
		Query query = this.getSession().createQuery(hql);
		if(values != null){
			for(int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		int currentPage = pageNo > 1 ? pageNo : 1;
		retValue.setCurrentPage(currentPage);
		retValue.setPageSize(pageSize);
		if (countHql == null)
		{
			ScrollableResults results = query.scroll();
			results.last();
			retValue.setTotalCount(results.getRowNumber() + 1);// 设置总记录数
		}
		else
		{
			Long count = countByHql(countHql, values);
			retValue.setTotalCount(count.intValue());
		}
		retValue.resetPageNo();
		List<T> itemList = query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
		if (itemList == null)
		{
			itemList = new ArrayList<T>();
		}
		retValue.setResults(itemList);

		return retValue;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 *
	 * @return session
	 */
	public Session getSession() {
		//需要开启事物，才能得到CurrentSession
		return sessionFactory.getCurrentSession();
	}

	/**
	 *
	 * 设置每行批处理参数
	 *
	 * @param ps
	 * @param pos ?占位符索引，从0开始
	 * @param data
	 * @see [类、类#方法、类#成员]
	 */
	private void setParameter(PreparedStatement ps, int pos, Object data)
			throws SQLException
	{
		if (data == null)
		{
			ps.setNull(pos + 1, Types.VARCHAR);
			return;
		}
		Class dataCls = data.getClass();
		if (String.class.equals(dataCls))
		{
			ps.setString(pos + 1, (String)data);
		}
		else if (boolean.class.equals(dataCls))
		{
			ps.setBoolean(pos + 1, ((Boolean)data));
		}
		else if (int.class.equals(dataCls))
		{
			ps.setInt(pos + 1, (Integer)data);
		}
		else if (double.class.equals(dataCls))
		{
			ps.setDouble(pos + 1, (Double)data);
		}
		else if (Date.class.equals(dataCls))
		{
			Date val = (Date)data;
			ps.setTimestamp(pos + 1, new Timestamp(val.getTime()));
		}
		else if (BigDecimal.class.equals(dataCls))
		{
			ps.setBigDecimal(pos + 1, (BigDecimal)data);
		}
		else
		{
			// 未知类型
			ps.setObject(pos + 1, data);
		}

	}
}
