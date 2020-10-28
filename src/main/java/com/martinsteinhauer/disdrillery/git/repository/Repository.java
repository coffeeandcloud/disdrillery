package com.martinsteinhauer.disdrillery.git.repository;

import java.io.File;

abstract class Repository {
    private File root;

    public Repository(File root) {
        this.root = root;
    }

    protected File getRoot() {
        return root;
    }
}
