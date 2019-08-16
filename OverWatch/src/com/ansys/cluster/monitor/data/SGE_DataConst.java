package com.ansys.cluster.monitor.data;

public final class SGE_DataConst {

	private SGE_DataConst() {
		// TODO Auto-generated constructor stub
	}

	public static String app_name = "OverWatch";
	public static String app_version = "1.4.4.0";
	public static String app_version_regex = "(\\d+\\.)+(\\d)";
	
	
	/**
	 * The name attribute of the elements
	 */
	public final static String json_host_name = "host";

	public final static String json_host_name_attrib = "name";

	public final static String json_arch = "arch_string";

	public final static String json_m_core = "m_core";

	public final static String json_m_socket = "m_socket";

	public final static String json_m_thread = "m_thread";

	public final static String json_mem_total = "mem_total";

	public final static String json_mem_used = "mem_used";

	public final static String json_np_load_avg = "np_load_avg";

	public final static String json_num_proc = "num_proc";

	public final static String json_qtype_string = "qtype_string";

	public final static String json_queue = "queue";

	public final static String json_slots = "slots";

	public final static String json_slots_resv = "slots_resv";

	public final static String json_slots_used = "slots_used";

	public final static String json_state_string = "state_string";

	public final static String json_swap_total = "swap_total";

	public final static String json_swap_used = "swap_used";

	public final static String json_qhost = "qhost";

	public final static String json_key_name = "@name";

	public final static String json_host_value = "hostvalue";

	public final static String json_job_detailed_info = "detailed_job_info";

	public final static String json_djob_info = "djob_info";

	public final static String json_job_info = "job_info";

	public final static String json_queue_info = "queue_info";

	public final static String json_job_list = "job_list";

	public final static String json_element = "element";

	public final static String json_task_id_range = "task_id_range";

	public final static String json_events = "Events";

	public final static String json_grl = "grl";
	
	public final static String[] json_sub_array = {json_element, json_task_id_range, json_events, json_grl};

	public final static String json_object = "JSONObject";

	public final static String json_array = "JSONArray";
	
	public final static String json_message = "messages";

	public final static String json_message_list = "SME_message_list";

	public final static String json_string_key = "$";

	public final static String json_string = "String";

	/**
	 * Job keys
	 */
	public final static String json_job_state_translated = "@state";

	public final static String json_job_number = "JB_job_number";

	public final static String json_job_priority = "JAT_prio";

	public final static String json_job_name = "JB_name";

	public final static String json_job_owner = "JB_owner";

	public final static String json_job_state = "state";

	public final static String json_job_submission_time = "JB_submission_time";

	public final static String json_job_start_time = "JAT_start_time";

	public final static String json_job_queue_name = "queue_name";

	public final static String json_job_jclass_name = "jclass_name";

	public final static String json_job_start_host = "start_host";
	
	public final static String msg = "messages";
	
	public final static String msg_msg_number = "MES_message_number";

	public final static String msg_msg = "MES_message";
	
	public final static String msg_job_list = "MES_job_number_list";
	
	public final static String xml_Element = json_element;
	
	public final static String djob_info = json_djob_info;

	
	/**
	 * NodeProp - Property Keys
	 */
	public final static String arch = json_arch;

	public final static String hostname = json_host_name;

	public final static String m_core = json_m_core;

	public final static String m_socket = json_m_socket;

	public final static String m_thread = json_m_thread;

	public final static String memFree = "memFree";

	public final static String memFreeNum = "memFreeNum";

	public final static String memTotal = json_mem_total;

	public final static String memTotalNum = json_mem_total + "_num";

	public final static String memUsed = json_mem_used;

	public final static String memUsedNum = json_mem_used + "_num";

	public final static String np_load_avg = json_np_load_avg;

	public final static String np_load_short = "np_load_short";

	public final static String np_load_long = "np_load_long";

	public final static String numProc = json_num_proc;

	public final static String qtype = json_qtype_string;

	public final static String queueName = json_queue;

	public final static String noNameHostQueue = "LIMBO";

	public final static String noNameJobQueue = "PENDING";
	
	public final static String job_ntq = "NonexisitngTargetQueue";

	public final static String queueType = json_qtype_string;

	public final static String slotRes = json_slots_resv;

	public final static String slotUsed = json_slots_used;

	public final static String slotTotal = json_slots;

	public final static String states = json_state_string;

	public final static String swap_total = json_swap_total;

	public final static String swap_used = json_swap_used;

	public final static String job_PendingQueue = "PENDING";

	public final static String job_MyJob = "Job_MyJob";
	
	public final static String myJob = "My Job";

	public final static String job_Messages = "Job_Messages";
	
	public static final String job_IdleThreshold = "com.ansys.monitor.job.idle.threshold";
	
	public static final String queueTarget = "queueTarget";
	
	public static final String attribName = "name";


	/**
	 * 
	 */
	public final static String mqEntryQueues = "QUEUE(S)";

	public final static String mqEntryJobs = "JOB(S)";

	/**
	 * SGE commands
	 */
	public final static String sgeQueueListCommand = "qconf -sql";

	public final static String sgeQueueStatCommand = "qstat -f -q ";

	public final static String sgeHostDetailCommand = "qhost -F -h  ";

	public final static String sgeAllHostXMLDetailCommand = "qhost -q -xml";

	/**
	 * Regex search
	 */
	public final static String regex_np_load_avg = "np_load_avg=(.+)";

	public final static String regex_np_load_short = "np_load_short=(.+)";

	public final static String regex_np_load_long = "np_load_long=(.+)";

	public final static String regex_mem_total = "hl:mem_total=(.+)";

	public final static String regex_mem_used = "hl:mem_used=(.+)";

	public final static String regex_mem_free = "hl:mem_free=(.+)";

	// Cluster Type
	public final static String clusterTypeCluster = "CLUSTER";
	public final static String clusterTypeHostMasterQueue = "HOST_MASTER_QUEUE";
	public final static String clusterTypeQueue = "QUEUE";
	public final static String clusterTypeHost = "HOST";
	public final static String clusterTypeJob = "JOB";
	
	public final static String connTypeHttp = "HTTP";
	public final static String connTypeFile = "FILE";
	public final static String connTypeCMD = "CMD";
	
	public final static String unitResCore = "Core(s)";
	public final static String unitResSession = "Session(s)";
	
	

}
