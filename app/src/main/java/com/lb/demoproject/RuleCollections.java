package com.lb.demoproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-24.
 */
public class RuleCollections {
    private int communityId;
    private List<RulesItem> items = new ArrayList<>();

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public List<RulesItem> getItems() {
        return items;
    }

    public void setItems(List<RulesItem> items) {
        this.items = items;
    }
}
