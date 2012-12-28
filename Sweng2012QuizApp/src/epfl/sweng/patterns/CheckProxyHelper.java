package epfl.sweng.patterns;

import epfl.sweng.services.impl.DefaultQuestionsService;
import epfl.sweng.services.proxy.QuestionsProxyService;

/**
 * 
 * @author thmx
 *
 */
public class CheckProxyHelper implements ICheckProxyHelper {

	@Override
	public Class<?> getServerCommunicationClass() {
		return DefaultQuestionsService.class;
	}

	@Override
	public Class<?> getProxyClass() {
		return QuestionsProxyService.class;
	}

}
