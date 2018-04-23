import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Challenge {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Input the number of sites(N):");
		int N = sc.nextInt();
		sc.close();
		if (N <= 0)
			System.out.println("Please enter the value greater than zero");
		else {
			List<String> sites = topSites(N);
			int i = 1;

			for (String site : sites) {
				System.out.println(i + " " + site);
				i++;
			}
		}
	}

	public static List<String> topSites(int N) {
		List<String> topList = new ArrayList<String>();
		String prefix = "http://topsites.eaiti.com/?page=";
		if (N <= 25) {
			topList.addAll(pagelinks(prefix + 0, N));
		} else {
			double pages = Math.ceil((N / 25.0));
			if (pages > 20)
				pages = 20;
			for (int i = 0; i < pages; i++) {
				String link = prefix + i;
				if (N > 25) {
					N -= 25;
					topList.addAll(pagelinks(link, 25));
				} else {
					topList.addAll(pagelinks(link, N));
				}
			}
		}
		return topList;
	}

	/***
	 * Function to fetch particular number of sites from the web page.
	 * @param link : Url to search for sites.
	 * @param n    : number of sites to be extracted and added to the list.
	 * @return List of top n sites from the HTML page.
	 */
	public static List<String> pagelinks(String link, int n) {
		List<String> pageList = new ArrayList<String>();
		boolean isException = true;
		int tries = 1;
		while (isException) {
			try {
				URL url = new URL(link);
				URLConnection connection = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String input;
				boolean content = false;
				while ((input = br.readLine()) != null && n > 0) {
					if (input.contains("<li class=\"site-item\">"))
						content = true;
					if (content && input.contains("<a href=\"/site/")) {
						String[] inputs = input.split("<a href=\"/site/");
						String[] string = inputs[1].split("\">");
						pageList.add(string[0]);
						n--;
					}
				}
				isException = false;
				br.close();

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				isException = true;
				pageList = new ArrayList<String>();
				System.out.println("Exception while connecting. Retrying...");
				e.printStackTrace();
			}

			tries++;
			if (tries > 10) {
				System.out.println("Maximum No. of tries attempted");
				break;
			}
		}
		return pageList;
	}
}
