package com.example.aplikacja_dyzury.security;

import com.example.aplikacja_dyzury.navAndThemes.NonRegisteredMenuBar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;


@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::beforeEnter);
		});
	}

	/**
	 * Reroutes the user if (s)he is not authorized to access the view.
	 *
	 * @param event
	 *            before navigation event with event details
	 */
	private void beforeEnter(BeforeEnterEvent event) {


		if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
			if(SecurityUtils.isUserLoggedIn()) {
				event.rerouteTo("accessDenied");
			} else {
				event.rerouteTo(NonRegisteredMenuBar.class);
			}
		}
	}
}