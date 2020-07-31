### Comments

1. Main entry point is the Launcher class, please use it to run both Randomizer and Prime applications via something like
```bash
# compile
javac -d out/production/TEK $(find . -name "*.java")
# run
java -cp out/production/TEK yara.Launcher 1101 1102
```
Two integers at the end of the sample (1101 and 1102) are the port numbers for Prime and Randomize application services. Use any two (but please do remember that port numbers less than 1024 require some special handling from root).

Press Enter twice in console to stop both applications correctly (releasing ports).

2. As I plan to deploy this on Git, I do not mention the company name anywhere in code/comments/package or class names to hide this solution from googling.

3. As it was specified that information will be passed via some sort of "distributed queues" but I was limited to "use only the standard Java library", I made an assumption that I need to simulate such "distributed queue". The named pipes was the initial choice but I've switched to the Sockets as I wanted the system to be an OS-agnostic (named pipes are not too good to Windows). Finally, the queue is simulated via Socket connections between Client and Server in both Randomizer -> Prime and Prime -> Randomizer channels.

4. It would be a bit faster to use the DataOutputStream.writeInt(...) and DataInputStream.readInt() methods for Randomizer -> Prime communication. But, as I needed to implement something Object-based anyway for the opposite way (Prime -> Randomizer), I've decided to unify communication in both directions. Specialized DataOutputStream should be faster but I've traded a bit or performance for better unification.

5. The `static` init block in `Util` class is being used to set a log level for the `yara` (highest level used in the project) package.

6. If something went wrong, clear your ports with:
```
lsof -i tcp:1101-1102 | grep java | awk '{print $2}' | xargs kill -9
```
