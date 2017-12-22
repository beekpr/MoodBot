package io.beekeeper.mood_bot;

import java.util.Iterator;
import java.util.List;

import io.beekeeper.sdk.BeekeeperSDK;
import io.beekeeper.sdk.exception.BeekeeperException;
import io.beekeeper.sdk.model.UserProfile;

public class ProfileIterator {

    private static final int LIMIT = 80;

    private final BeekeeperSDK sdk;

    private Iterator<UserProfile> iterator;
    private int offset = 0;

    private UserProfile next = null;
    private UserProfile current = null;

    public ProfileIterator(BeekeeperSDK sdk) throws BeekeeperException {
        this.sdk = sdk;
        advance();
    }

    public boolean hasNext() {
        return next != null;
    }

    public void advanceToNext() throws BeekeeperException {
        if (!hasNext()) {
            throw new RuntimeException();
        }
        advance();
    }

    private void advance() throws BeekeeperException {
        current = next;
        if (iterator != null && iterator.hasNext()) {
            next = iterator.next();
        } else {
            advanceToNextBatch();
            if (iterator.hasNext()) {
                next = iterator.next();
            } else {
                next = null;
            }
        }
    }

    private void advanceToNextBatch() throws BeekeeperException {
        List<UserProfile> profiles = sdk.getProfiles().getProfiles(offset, LIMIT).execute();

        offset += profiles.size();
        iterator = profiles.iterator();
    }

    public UserProfile getCurrent() {
        return current;
    }

}