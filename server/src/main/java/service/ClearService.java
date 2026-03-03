package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class ClearService {
    public DataAccess data;

    public ClearService(DataAccess data){
        this.data = data;
    }

    public void clear() throws DataAccessException {
        data.clear();
    }

}
