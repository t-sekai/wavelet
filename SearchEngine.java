import java.io.IOException;
import java.net.URI;
import java.util.*;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;
    ArrayList<String> string;
    
    public String handleRequest(URI url) {
        if(string == null) string = new ArrayList<>();
        
        if (url.getPath().equals("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append("Welcome to the local browser! \n");
            sb.append("The following is the list of " + num +  " strings you have added: \n");
            for(String s : string){
                sb.append(s + "\n");
            }
            return sb.toString();
        } else if (url.getPath().equals("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                string.add(parameters[1]);
                ++num;
                return parameters[1] + " has been added to the list.";
            }
        } else if (url.getPath().equals("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                StringBuilder sb = new StringBuilder();
                int cnt = 0;
                sb.append("The following is the list of %d strings that contains your inputted string: \n");
                for(String s : string){
                    if(s.contains(parameters[1])){
                        sb.append(s + "\n");
                        ++cnt;
                    }
                }
                return String.format(sb.toString(),cnt);
            }
        }else {
            System.out.println("Path: " + url.getPath());
        }
        return "404 Not Found!";
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Server.start(port, new Handler());
    }
}
