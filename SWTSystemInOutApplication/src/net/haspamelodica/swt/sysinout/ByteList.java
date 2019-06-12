package net.haspamelodica.swt.sysinout;

import java.util.Arrays;

public class ByteList
{
	private byte[]	arr;
	private int		size;

	public ByteList()
	{
		arr = new byte[10];
	}

	public void add(byte[] bytes)
	{
		growArrayIfNeccessary(size + bytes.length);
		System.arraycopy(bytes, 0, arr, size, bytes.length);
		size += bytes.length;
	}
	public void add(byte b)
	{
		growArrayIfNeccessary(size + 1);
		arr[size ++] = b;
	}
	public int size()
	{
		return size;
	}
	public byte[] array()
	{
		return arr;
	}
	private void growArrayIfNeccessary(int length)
	{
		if(arr.length < length)
			arr = Arrays.copyOf(arr, length * 2);
	}
}