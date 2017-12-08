package com.pubnub.api.builder;

import com.pubnub.api.builder.dto.HeartbeatOperation;
import com.pubnub.api.managers.SubscriptionManager;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class DeregisterHeartbeatBuilder extends PubSubBuilder {


    public DeregisterHeartbeatBuilder(SubscriptionManager subscriptionManagerInstance) {
        super(subscriptionManagerInstance);
    }

    @Override
    public void execute() {
        HeartbeatOperation heartbeatOperation = HeartbeatOperation.builder()
                .channels(this.getChannelSubscriptions())
                .channelGroups(this.getChannelGroupSubscriptions())
                .build();

        this.getSubscriptionManager().adaptDeRegisterHeartbeatBuilder(heartbeatOperation);
    }
}
