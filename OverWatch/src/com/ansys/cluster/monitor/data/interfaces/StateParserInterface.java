package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;

public interface StateParserInterface extends  Serializable {

	StateAbstract parseCode(char code);

}