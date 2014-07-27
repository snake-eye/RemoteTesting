import java.io.*;

public class a {
	public static void main(String[] args) {
		try {
			String buffer = null;
			FileOutputStream out = new FileOutputStream("R.TXT");

			if (args.length != 3) {
				buffer = "Usage:a n op n";
				out.write(buffer.getBytes());
				out.close();
				System.exit(0);
			}
			try {
				double a = Double.parseDouble(args[0]);
				double b = Double.parseDouble(args[2]);

				if (args[1].equalsIgnoreCase("+"))
					buffer = String.valueOf(a + b);
				else if (args[1].equalsIgnoreCase("-"))
					buffer = String.valueOf(a - b);
				else if (args[1].equalsIgnoreCase("*"))
					buffer = String.valueOf(a * b);
				else if (args[1].equalsIgnoreCase("/"))
					buffer = String.valueOf(a / b);
				else
					buffer = "+ - * / required";
			} catch (Exception e) {
				buffer = e.getMessage();
			}
			out.write(buffer.getBytes());
			out.flush();
			out.close();
		} catch (Exception ex) {
		}
	}
}