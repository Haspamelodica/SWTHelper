package net.haspamelodica.swt.sysinout;

public class ByteQueue
{
	private byte[]	buf;
	private int		from;
	private int		size;

	public ByteQueue()
	{
		this.buf = new byte[100];
		this.from = 0;
		this.size = 0;
	}

	public void offer(byte b)
	{
		growIfNeeded(1);
		synchronized(buf)
		{
			buf[(from + size ++) % buf.length] = b;
			buf.notify();
		}
	}
	public void offer(byte[] bytes, int off, int size)
	{
		growIfNeeded(size);
		//TODO make this faster
		for(int i = off; i < off + size; i ++)
			offer(bytes[i]);
	}
	public void offer(byte[] bytes)
	{
		offer(bytes, 0, bytes.length);
	}
	public byte pollBlocking() throws InterruptedException
	{
		synchronized(buf)
		{
			while(size == 0)
				buf.wait();
			byte val = buf[from];
			from = (from + 1) % buf.length;
			size --;
			return val;
		}
	}
	public int pollSemiBlocking(byte[] bytes, int off, int len) throws InterruptedException
	{
		synchronized(buf)
		{
			while(size == 0)
				buf.wait();
			int actuallyPolled = Math.min(len, size);
			//TODO make this faster
			for(int i = off; i < off + actuallyPolled; i ++)
			{
				bytes[i] = buf[from];
				from = (from + 1) % buf.length;
			}
			size -= actuallyPolled;
			return actuallyPolled;
		}
	}
	private void growIfNeeded(int spaceRequest)
	{
		if(buf.length - size < spaceRequest)
		{
			byte[] bufOld = buf;
			buf = new byte[spaceRequest * 2];
			if(size > 0)
			{
				if(from + size < buf.length)
					System.arraycopy(bufOld, from, buf, 0, size);
				else
				{
					System.arraycopy(bufOld, from, buf, 0, buf.length - from);
					System.arraycopy(bufOld, 0, buf, buf.length - from, size - (buf.length - from));
				}
				from = 0;
			}
		}
	}
}