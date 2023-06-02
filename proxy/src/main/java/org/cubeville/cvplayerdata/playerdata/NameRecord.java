package org.cubeville.cvplayerdata.playerdata;

import java.util.UUID;

public class NameRecord implements Comparable<NameRecord> {

    private UUID uuid;
    private String name;
    private long nameTime;

    public NameRecord(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public long getNameTime() {
        return nameTime;
    }

    public void setNameTime(long nameTime) {
        this.nameTime = nameTime;
    }

    public int compareTo(NameRecord other) {
        return Long.compare(this.nameTime, other.nameTime);
    }
}
