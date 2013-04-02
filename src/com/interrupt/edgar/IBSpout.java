package com.interrupt.edgar;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.IRichSpout;


public class IBSpout implements IRichSpout {


  /**
   * Storm spout stuff
   */
  private SpoutOutputCollector _collector;
  
  private List<Object> _tuple = new ArrayList<Object>();
  public void setTuple(List<Object> tuple) { _tuple = tuple; }
  public List<Object> getTuple() { return _tuple; }
  
  /**
   * Storm ISpout interface functions
   */
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    _collector = collector;
  }
  public void close() {}
  public void activate() {}
  public void deactivate() {}
  public void nextTuple() {
    _collector.emit(_tuple);
    _tuple.clear();
  }
  public void ack(Object msgId) {}
  public void fail(Object msgId) {}
  
  
  public void declareOutputFields(OutputFieldsDeclarer declarer) {}
  public java.util.Map<java.lang.String,java.lang.Object>  getComponentConfiguration() { return new HashMap(); }
  
}
