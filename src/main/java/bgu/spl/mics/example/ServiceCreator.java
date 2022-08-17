package bgu.spl.mics.example;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;

import java.util.concurrent.ConcurrentHashMap;

public interface ServiceCreator {
    MicroService create(String name, String[] args);
}
