package net.thecamaras.domain;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by rcamara on 1/20/2016.
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class State {
    public boolean active = true;
}
