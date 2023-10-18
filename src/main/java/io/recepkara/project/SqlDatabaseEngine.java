package io.recepkara.project;

import io.recepkara.project.csv.beans.SearchResult;

public interface SqlDatabaseEngine {
    SearchResult executeSelect(String sql);
    void execute(String sql);
    int executeUpdate(String sql);
}
