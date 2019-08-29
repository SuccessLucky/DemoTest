package com.project.common.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDao extends HibernateDaoSupport {

	public List findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List findByCriteria(DetachedCriteria criteria, int firstResultIndex, int numberInOnePage) {
		return getHibernateTemplate().findByCriteria(criteria, firstResultIndex, numberInOnePage);
	}

	public Object execute(HibernateCallback action) {
		return getHibernateTemplate().execute(action);
	}

	public List findByQuery(final String Hql) {
		return (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(Hql);
				return query.list();
			}
		});
	}

	public Integer executeDelByQuery(final String Hql, final String pameterName, final Integer[] parameter) {
		return (Integer) execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(Hql);
				query.setParameterList(pameterName, parameter);
				return query.executeUpdate();
			}
		});
	}

	public Integer executeByQuery(final String Hql) {
		return (Integer) execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery(Hql).executeUpdate();
			}
		});
	}

	public List findByQueryOfSql(final String sql) {
		return (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	public List execute(final String Sql) {
		return (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				session.createSQLQuery(Sql).executeUpdate();
				return null;
			}
		});
	}

	public Session getHibernateSession() {
		return getSessionFactory().openSession();
	}

	public Connection getConnection() {
		try {
			return SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();
		} catch (SQLException e) {
		}
		return null;
	}
}
