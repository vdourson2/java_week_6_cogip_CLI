package week6.java.cogipCli.cogip_CLI.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CompanyCommand {
	
	@ShellMethod(key="hello", value="Say hello")
	public String Hello(@ShellOption (defaultValue="World") String arg) {
		return "Hello, " + arg;
	}

}
