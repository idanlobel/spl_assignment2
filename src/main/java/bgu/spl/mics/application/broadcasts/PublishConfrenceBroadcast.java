package bgu.spl.mics.application.broadcasts;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

import java.util.List;

public class PublishConfrenceBroadcast implements Broadcast {
    List<Event> listOfGoodModels;

    public PublishConfrenceBroadcast(List<Event> listOfGoodModels) {
        this.listOfGoodModels=listOfGoodModels;
    }
}
