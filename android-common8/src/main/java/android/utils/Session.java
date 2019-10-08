package android.utils;

import java.util.HashMap;
import java.util.Map;

public class Session
{

	private Map<String, Object> _objectContainer;

	/**
	 * 互斥锁
	 * 
	 * @return
	 */
	private static Session session;

	private Session()
	{
		_objectContainer = new HashMap<String, Object>();
	}

	public static synchronized Session getSession()
	{

		if (session == null)
		{
			session = new Session();
			return session;
		}
		else
		{
			return session;
		}
	}

	public void put(String key, Object value)
	{

		_objectContainer.put(key, value);
	}

	public Object get(String key)
	{

		return _objectContainer.get(key);
	}

	public void cleanUpSession()
	{
		_objectContainer.clear();
	}

	public void remove(String key)
	{
		_objectContainer.remove(key);
	}
}
