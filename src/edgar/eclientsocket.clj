(ns edgar.eclientsocket)

(defn create-eclientsocket []

  (comment

    public com.ib.client.EClientSocket(com.ib.client.AnyWrapper);
    public boolean isConnected();
    public synchronized void eConnect(java.lang.String, int, int);
    protected void connectionError();
    protected java.lang.String checkConnected(java.lang.String);
    public com.ib.client.EReader createReader(com.ib.client.EClientSocket, java.io.DataInputStream);
    public synchronized void eConnect(java.net.Socket, int)       throws java.io.IOException;
    public synchronized void eDisconnect();
    public synchronized void cancelScannerSubscription(int);
    public synchronized void reqScannerParameters();
    public synchronized void reqScannerSubscription(int, com.ib.client.ScannerSubscription);
    public synchronized void reqMktData(int, com.ib.client.Contract, java.lang.String, boolean);
    public synchronized void cancelHistoricalData(int);
    public void cancelRealTimeBars(int);
    public synchronized void reqHistoricalData(int, com.ib.client.Contract, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int);
    public synchronized void reqRealTimeBars(int, com.ib.client.Contract, int, java.lang.String, boolean);
    public synchronized void reqContractDetails(int, com.ib.client.Contract);
    public synchronized void reqMktDepth(int, com.ib.client.Contract, int);
    public synchronized void cancelMktData(int);
    public synchronized void cancelMktDepth(int);
    public synchronized void exerciseOptions(int, com.ib.client.Contract, int, int, java.lang.String, int);
    public synchronized void placeOrder(int, com.ib.client.Contract, com.ib.client.Order);
    public synchronized void reqAccountUpdates(boolean, java.lang.String);
    public synchronized void reqExecutions(int, com.ib.client.ExecutionFilter);
    public synchronized void cancelOrder(int);
    public synchronized void reqOpenOrders();
    public synchronized void reqIds(int);
    public synchronized void reqNewsBulletins(boolean);
    public synchronized void cancelNewsBulletins();
    public synchronized void setServerLogLevel(int);
    public synchronized void reqAutoOpenOrders(boolean);
    public synchronized void reqAllOpenOrders();
    public synchronized void reqManagedAccts();
    public synchronized void requestFA(int);
    public synchronized void replaceFA(int, java.lang.String);
    public synchronized void reqCurrentTime();
    public synchronized void reqFundamentalData(int, com.ib.client.Contract, java.lang.String);
    public synchronized void cancelFundamentalData(int);
    public synchronized void calculateImpliedVolatility(int, com.ib.client.Contract, double, double);
    public synchronized void cancelCalculateImpliedVolatility(int);
    public synchronized void calculateOptionPrice(int, com.ib.client.Contract, double, double);
    public synchronized void cancelCalculateOptionPrice(int);
    public synchronized void reqGlobalCancel();
    public synchronized void reqMarketDataType(int);
    protected synchronized void error(java.lang.String);
    protected synchronized void error(int, int, java.lang.String);
    protected void close();
    protected void send(java.lang.String)       throws java.io.IOException;
    protected void send(int)       throws java.io.IOException;
    protected void send(char)       throws java.io.IOException;
    protected void send(double)       throws java.io.IOException;
    protected void send(long)       throws java.io.IOException;
    protected void send(boolean)       throws java.io.IOException;

    )
  )
