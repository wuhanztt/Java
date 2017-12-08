package com.pubnub.api.managers;


import com.pubnub.api.PNConfiguration;
import com.pubnub.api.builder.dto.HeartbeatOperation;
import com.pubnub.api.builder.dto.StateOperation;
import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.builder.dto.UnsubscribeOperation;
import com.pubnub.api.models.SubscriptionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateManager {

    private PNConfiguration pnConfiguration;

    /**
     * Contains a list of subscribed channels
     */
    private Map<String, SubscriptionItem> channels;
    /**
     * Contains a list of subscribed presence channels.
     */
    private Map<String, SubscriptionItem> presenceChannels;

    /**
     * Contains a list of subscribed channel groups.
     */
    private Map<String, SubscriptionItem> groups;

    /**
     * Contains a list of subscribed presence channel groups.
     */
    private Map<String, SubscriptionItem> presenceGroups;


    /**
     * Heartbeat related items
     */
    private Map<String, SubscriptionItem> heartbeatChannels;
    private Map<String, SubscriptionItem> heartbeatChannelGroups;

    public StateManager(PNConfiguration pnConfiguration) {
        this.channels = new HashMap<>();
        this.presenceChannels = new HashMap<>();

        this.groups = new HashMap<>();
        this.presenceGroups = new HashMap<>();

        this.heartbeatChannels = new HashMap<>();
        this.heartbeatChannelGroups = new HashMap<>();

        this.pnConfiguration = pnConfiguration;
    }


    public synchronized void adaptSubscribeBuilder(SubscribeOperation subscribeOperation) {
        for (String channel : subscribeOperation.getChannels()) {
            if (channel == null || channel.length() == 0) {
                continue;
            }

            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channel);
            channels.put(channel, subscriptionItem);

            if (subscribeOperation.isPresenceEnabled()) {
                SubscriptionItem presenceSubscriptionItem = new SubscriptionItem().setName(channel);
                presenceChannels.put(channel, presenceSubscriptionItem);
            }

            if (pnConfiguration.isHeartbeatOnAllSubscriptions()) {
                heartbeatChannels.put(channel, subscriptionItem);
            }

        }

        for (String channelGroup : subscribeOperation.getChannelGroups()) {
            if (channelGroup == null || channelGroup.length() == 0) {
                continue;
            }

            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channelGroup);
            groups.put(channelGroup, subscriptionItem);

            if (subscribeOperation.isPresenceEnabled()) {
                SubscriptionItem presenceSubscriptionItem = new SubscriptionItem().setName(channelGroup);
                presenceGroups.put(channelGroup, presenceSubscriptionItem);
            }

            if (pnConfiguration.isHeartbeatOnAllSubscriptions()) {
                heartbeatChannels.put(channelGroup, subscriptionItem);
            }

        }
    }

    public synchronized void adaptStateBuilder(StateOperation stateOperation) {
        for (String channel: stateOperation.getChannels()) {
            SubscriptionItem subscribedChannel = channels.get(channel);

            if (subscribedChannel != null) {
                subscribedChannel.setState(stateOperation.getState());
            }
        }

        for (String channelGroup: stateOperation.getChannelGroups()) {
            SubscriptionItem subscribedChannelGroup = groups.get(channelGroup);

            if (subscribedChannelGroup != null) {
                subscribedChannelGroup.setState(stateOperation.getState());
            }
        }
    }

    public synchronized void adaptUnsubscribeBuilder(UnsubscribeOperation unsubscribeOperation) {
        for (String channel: unsubscribeOperation.getChannels()) {
            this.channels.remove(channel);
            this.presenceChannels.remove(channel);

            if (pnConfiguration.isHeartbeatOnAllSubscriptions()) {
                this.heartbeatChannels.remove(channel);
            }

        }

        for (String channelGroup: unsubscribeOperation.getChannelGroups()) {
            this.groups.remove(channelGroup);
            this.presenceGroups.remove(channelGroup);

            if (pnConfiguration.isHeartbeatOnAllSubscriptions()) {
                this.heartbeatChannelGroups.remove(channelGroup);
            }

        }
    }

    public synchronized  void adaptRegisterHeartbeat(HeartbeatOperation heartbeatOperation) {
        for (String channel : heartbeatOperation.getChannels()) {
            if (channel == null || channel.length() == 0) {
                continue;
            }

            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channel);
            channels.put(channel, subscriptionItem);
        }

        for (String channelGroup : heartbeatOperation.getChannelGroups()) {
            if (channelGroup == null || channelGroup.length() == 0) {
                continue;
            }

            SubscriptionItem subscriptionItem = new SubscriptionItem().setName(channelGroup);
            heartbeatChannelGroups.put(channelGroup, subscriptionItem);
        }

    }

    public synchronized void adaptDeregisterHeartbeat(HeartbeatOperation heartbeatOperation) {
        for (String channel: heartbeatOperation.getChannels()) {
            this.heartbeatChannels.remove(channel);
        }

        for (String channelGroup: heartbeatOperation.getChannelGroups()) {
            this.heartbeatChannelGroups.remove(channelGroup);
        }

    }

    public synchronized Map<String, Object> createStatePayload() {
        Map<String, Object> stateResponse = new HashMap<>();

        for (SubscriptionItem channel: channels.values()) {
            if (channel.getState() != null) {
                stateResponse.put(channel.getName(), channel.getState());
            }
        }

        for (SubscriptionItem channelGroup: groups.values()) {
            if (channelGroup.getState() != null) {
                stateResponse.put(channelGroup.getName(), channelGroup.getState());
            }
        }

        return stateResponse;
    }

    public synchronized List<String> prepareChannelList(boolean includePresence) {
        return prepareMembershipList(channels, presenceChannels, includePresence);
    }

    public synchronized List<String> prepareChannelGroupList(boolean includePresence) {
        return prepareMembershipList(groups, presenceGroups, includePresence);
    }

    public synchronized List<String> getHeartbeatChannelList() {
         return prepareMembershipList(heartbeatChannels, null, false);
    }

    public synchronized List<String> getHeartbeatChannelGroupList() {
        return prepareMembershipList(heartbeatChannelGroups, null, false);
    }

    public synchronized boolean isEmpty() {
        return (channels.isEmpty() && presenceChannels.isEmpty() && groups.isEmpty() && presenceGroups.isEmpty());
    }

    private synchronized List<String> prepareMembershipList(Map<String, SubscriptionItem> dataStorage, Map<String, SubscriptionItem> presenceStorage, boolean includePresence) {
        List<String> response = new ArrayList<>();

        for (SubscriptionItem channelGroupItem: dataStorage.values()) {
            response.add(channelGroupItem.getName());
        }

        if (includePresence) {
            for (SubscriptionItem presenceChannelGroupItem: presenceStorage.values()) {
                response.add(presenceChannelGroupItem.getName().concat("-pnpres"));
            }
        }


        return response;
    }

}
