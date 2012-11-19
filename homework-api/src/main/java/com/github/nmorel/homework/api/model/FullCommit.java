package com.github.nmorel.homework.api.model;

public class FullCommit
{
    private User committer;

    private Commit commit;

    public User getCommitter()
    {
        return committer;
    }

    public Commit getCommit()
    {
        return commit;
    }

}
