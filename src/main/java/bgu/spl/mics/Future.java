package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

import bgu.spl.mics.application.objects.Model;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 *
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

	private T result; //  not sure if Model
	private boolean result_is_ready;

	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {

		result_is_ready=false;
		this.result=null;
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved.
	 * This is a blocking method! It waits for the computation in case it has
	 * not been completed.
	 * <p>
	 * @return return the result of type T if it is available, if not wait until it is available.
	 *
	 */


	public T get() {
		return this.result;
	}

	/**
	 * Resolves the result of this Future object.
	 * @PRE:this!=null
	 * @POST:this.result=result
	 */
	public void resolve (T result) {
		//maybe add resolve method to Model


		this.result_is_ready=true;
		this.result=result;
	}

	/**
	 * @return true if this object has been resolved, false otherwise
	 */
	public boolean isDone() {

		return result_is_ready;
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved,
	 * This method is non-blocking, it has a limited amount of time determined
	 * by {@code timeout}
	 * <p>
	 * @param timeout 	the maximal amount of time units to wait for the result.
	 * @param unit		the {@link TimeUnit} time units to wait.
	 * @return return the result of type T if it is available, if not,
	 * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
	 *         elapsed, return null.
	 * @PRE:timeout>0&&unit!=null
	 * @POST:@return=this.result
	 */
	public T get(long timeout, TimeUnit unit)  {

		if(this.result!=null)
		{
			return this.result;
		}
		else {
			try {
				unit.wait(timeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.result!=null)
			{
				return this.result;
			}


		}


		return null;
	}

}