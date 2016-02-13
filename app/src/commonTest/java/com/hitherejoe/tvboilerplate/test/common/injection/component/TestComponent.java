package com.hitherejoe.tvboilerplate.test.common.injection.component;

import com.hitherejoe.tvboilerplate.injection.component.ApplicationComponent;
import com.hitherejoe.tvboilerplate.test.common.injection.module.ApplicationTestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}