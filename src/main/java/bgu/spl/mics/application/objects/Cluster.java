package bgu.spl.mics.application.objects;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	/**
	 * Retrieves the single instance of this class.
	 */
	private static Cluster instanceofcluster;
	private List<GPU> listofgpu;
	private List<CPU> listofcpu;
	private List<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer>> listofpairs;
	private HashMap<Integer,Integer> tabular_proccessed_databatch;


	private Cluster()
	{
		this.listofcpu=new ArrayList<CPU>();
		this.listofgpu=new ArrayList<GPU>();
		this.listofpairs=new ArrayList<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer>>();
		this.tabular_proccessed_databatch=new HashMap<Integer,Integer>();
	}


	public static Cluster getInstance() {
		if(instanceofcluster==null)
		{
			instanceofcluster=new Cluster();
		}

		return instanceofcluster;
	}

	public void add_lists(List<CPU> cpulist ,List<GPU> gpulist)
	{
		this.listofgpu=gpulist;
		this.listofcpu=cpulist;
		HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
		for(GPU gpu:this.listofgpu)
		{
			map.put(gpu.GetID(), 0);
		}
		this.tabular_proccessed_databatch=map;

	}

	public void get_from_gpu(AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer> pair)
	{

		this.listofpairs.add(pair);

	}


	public void send_to_cpu()
	{

		if(!this.listofpairs.isEmpty())
		{

			CPU cpu_with_min_ratio=this.listofcpu.get(0);//ratio is :   cores/(data to be proccessed)
			int current_ratio=cpu_with_min_ratio.waiting_databatches()/cpu_with_min_ratio.get_number_of_cores();

			for(CPU cpu:this.listofcpu)
			{
				if(cpu.waiting_databatches()/cpu.get_number_of_cores()<current_ratio)
				{
					cpu_with_min_ratio=cpu;
				}
			}


			cpu_with_min_ratio.get_from_cluster(this.listofpairs.remove(0));
		}

	}


//	public HashMap<Integer,List<DataBatch>> get_hash()
//	{
//
//		return this.tabular_proccessed_databatch;
//	}

	public boolean  check_if_completed()
	{//gets pair of <ID,size of list>
		for(Integer id:this.tabular_proccessed_databatch.keySet())
		{
			for(GPU gpu:this.listofgpu)
			{
				if(id==gpu.GetID())
				{
					if(this.tabular_proccessed_databatch.get(id)==gpu.get_supposed_proccessed())
					{
						return true;

					}
				}

			}

		}
		return false;
	}





//	public AbstractMap.SimpleEntry<DataBatch,Integer> sendtocpu()
//	{
//		if(!this.listofpairs.isEmpty())
//		{
//			return this.listofpairs.remove(0);
//		}
//		return null;
//	}
//





//	public void get_from_gpu(AbstractMap.SimpleEntry<DataBatch,Integer> pair)
//	{
//
//		CPU cpu_with_min_ratio=this.listofcpu.get(0);//ratio is :   cores/(data to be proccessed)
//		int current_ratio=cpu_with_min_ratio.waiting_databatches()/cpu_with_min_ratio.get_number_of_cores();
//
//		for(CPU cpu:this.listofcpu)
//		{
//			if(cpu.waiting_databatches()/cpu.get_number_of_cores()<current_ratio)
//			{
//				cpu_with_min_ratio=cpu;
//			}
//		}
//		cpu_with_min_ratio.get_from_cluster(pair);
//
//	}
//


	public void get_from_cpu(AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer> pair,CPU cpu)
	{
		for(GPU gpu:this.listofgpu)
		{
			if(gpu.GetID()==pair.getValue())
			{
				if(!gpu.isfull())
				{
					gpu.get_from_cluster(pair);
					Integer temp=this.tabular_proccessed_databatch.get(gpu.GetID());
					this.tabular_proccessed_databatch.put(gpu.GetID(), temp+1);
					break;
				}
				else
				{
					cpu.get_from_cluster_proccessed(pair);
				}

			}

		}



	}






}