package de.fhws.ai.utility.throwingintefaces;

public interface ThrowingRunnable<E extends Exception>
{
	void run() throws E;

	static <T, E extends Exception> Runnable unchecked(ThrowingRunnable<E> throwingRunnable)
	{
		return () -> {
			try
			{
				throwingRunnable.run();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		};
	}

}
