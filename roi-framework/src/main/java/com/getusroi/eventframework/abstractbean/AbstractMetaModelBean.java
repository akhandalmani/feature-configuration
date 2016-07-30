package com.getusroi.eventframework.abstractbean;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.metamodel.CompositeDataContext;
import org.apache.metamodel.DataContext;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.schema.TableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMetaModelBean extends AbstractROICamelBean{
	protected static final Logger logger = LoggerFactory.getLogger(AbstractROICamelBean.class);
	private DataSource dataSource=null;	
	private List<DataSource> dataSourceList;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	public void setDataSourceList(List<DataSource> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public JdbcDataContext getMetaModelJdbcDataContext(Exchange exchange) throws Exception{
		logger.debug(".getMetaModelJdbcDataContext method of AbstractMetaModelBean");
		Connection con=getConnection(dataSource, exchange);
		logger.debug("AbstractMetaModelBean.getMetaModelJdbcDataContext got the connection : "+con);
		JdbcDataContext metamodelJdbcContext=new JdbcDataContext(con);
		metamodelJdbcContext.setIsInTransaction(true);
		return metamodelJdbcContext;
	}	
	
	public DataContext getCompositeMetaModelDataContext(Exchange exchange) throws Exception{
		logger.debug(".getCompositeMetaModelDataContext method of AbstractMetaModelBean");
		Collection<DataContext> coll=new ArrayList<>();
		logger.debug("DataSource List : "+dataSourceList);
		if(dataSourceList!=null && !(dataSourceList.isEmpty())){
			for(DataSource datasource:dataSourceList){
				Connection connection=getConnection(datasource, exchange);
				logger.debug("AbstractMetaModelBean.getCompositeMetaModelDataContext got the connection : "+connection);
				DataContext metamodelJdbcContext=new JdbcDataContext(connection);
				coll.add(metamodelJdbcContext);
			}
		}		
		DataContext dataContextComposite=new CompositeDataContext(coll);
		return dataContextComposite;
	}
	
	
	public JdbcDataContext getMetaModelJdbcDataContext( TableType[] tableTypes, String catalogName,Exchange exchange) throws Exception{
		//#TODO Support is not yet Provided
		return null;
	}
	

	@Override
	protected abstract void processBean(Exchange exch) throws Exception;

}
