package com.apitest.common

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener

abstract class AutoBean:ApplicationListener<ApplicationEvent> {
    override fun onApplicationEvent(event: ApplicationEvent?) {

    }
}