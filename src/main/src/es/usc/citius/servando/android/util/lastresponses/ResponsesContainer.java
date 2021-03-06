package es.usc.citius.servando.android.util.lastresponses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import es.usc.citius.servando.android.logging.ILog;
import es.usc.citius.servando.android.logging.ServandoLoggerFactory;

@Root(name = "container")
public class ResponsesContainer {

	ILog logger = ServandoLoggerFactory.getLogger(ResponsesContainer.class);
	@ElementMap(name = "responses", keyType = String.class, valueType = ResponsesWrapper.class, inline = true, required = false)
	HashMap<String, ResponsesWrapper> wrappers;

	public ResponsesContainer()
	{
		wrappers = new HashMap<String, ResponsesWrapper>();
	}

	public HashMap<String, ResponsesWrapper> getWrappers()
	{
		return wrappers;
	}

	public void setWrappers(HashMap<String, ResponsesWrapper> wrappers)
	{
		this.wrappers = wrappers;
	}

	public void addResponse(String itemId, Response response)
	{

		ResponsesWrapper last = this.wrappers.get(itemId);
		// Check for nulls
		if (last == null)
		{
			last = new ResponsesWrapper();
			this.wrappers.put(itemId, last);
		}

		if (response != null && last != null && last.getResponses() != null)
		{
			// If the list is not full, insert the response
			if (last.getResponses().size() < ResponsesWrapper.MAX_RESPONSES)
			{
				last.getResponses().add(response);
			} else
			// if list is full, delete the first response (the oldest) and insert at the end
			{
				last.getResponses().remove(0);
				last.getResponses().add(response);
			}
		}
	}

	public Response getLastResponse(String itemId)
	{

		if (this.wrappers != null)
		{
			ResponsesWrapper wrapper = this.wrappers.get(itemId);
			if (wrapper != null)
			{
				List<Response> list = wrapper.getResponses();
				if (list != null && list.size() > 0)
				{
					return list.get(list.size() - 1);
				}
			}
		}
		return null;
	}

	/**
	 * This method return the last 'x' responses for a itemId. If in this moment there are less than this 'x' responses
	 * the method return all responses. If there are not responses returns an empty list.
	 * 
	 * @param itemId
	 * @param lasts Number os responses. Should be more than 1.
	 * @return
	 */
	public List<Response> getLastResponses(String itemId, int lasts)
	{
		List<Response> toReturn = new ArrayList<Response>();

		ResponsesWrapper lastResponses = this.wrappers.get(itemId);
		if (lastResponses != null)
		{
			if (lastResponses.getResponses().size() <= lasts)
			{
				toReturn = new ArrayList<Response>(lastResponses.getResponses());
			} else
			// If have enough response return only the requested
			{
				int size = lastResponses.getResponses().size();
				int start = size - lasts;
				toReturn = new ArrayList<Response>(lastResponses.getResponses().subList(start, size));
			}

		}
		return toReturn;
	}

	/**
	 * This method returns a list of responses for this itemId that was saved after the date
	 * 
	 * @param itemId
	 * @param date
	 * @return
	 */
	public List<Response> getResponsesAfter(String itemId, Date date)
	{
		List<Response> toReturn = new ArrayList<Response>();
		try
		{
			ResponsesWrapper wrapper = this.wrappers.get(itemId);
			if (wrapper != null)
			{
				DateTime mark = new DateTime(date);
				for (Response response : wrapper.getResponses())
				{
					DateTime current = new DateTime(response.getTimeStamp());
					if (current.isAfter(mark))
					{
						toReturn.add(response);
					}
				}
			}
		} catch (Exception e)
		{
			// Si se produce una excepción devolvemos la lista vacía
			toReturn = new ArrayList<Response>();
			logger.error("Error getting responses after a date.", e);
		}
		return toReturn;
	}
}
