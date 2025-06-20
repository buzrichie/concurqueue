package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
public class Task implements Comparable<Task> {
    private final UUID id;
    private final String name;
    private final int priority;
    private final Instant createdTimestamp;
    private final String payload;
    private int retryCount;

    public void incrementRetryCount() {
        this.retryCount++;
    }


    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }
}

