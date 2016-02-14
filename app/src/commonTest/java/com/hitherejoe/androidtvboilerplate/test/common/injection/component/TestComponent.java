package com.hitherejoe.androidtvboilerplate.test.common.injection.component;

import com.hitherejoe.androidtvboilerplate.injection.component.ApplicationComponent;
import com.hitherejoe.androidtvboilerplate.test.common.injection.module.ApplicationTestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}