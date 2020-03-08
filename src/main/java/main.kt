import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

object Main {

    fun String.runCommand(workingDir: File) {
        println("Path to run in: ${workingDir.absoluteFile}")
        ProcessBuilder(*split(" ").toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
    }
    var validationKey = "_insert_key_"
    @JvmStatic
    fun main(args: Array<String>) {
        //Read the json file,
        //create directories for each account to run the JAR
        //create the subset json for that account
        //launch jar with auth key
        var getNextKey = false
        args.iterator().forEach {
            if(getNextKey){
                validationKey = it
            }
            if(it == "-key"){
                getNextKey = true
            }
            println(it)
        }

        val curDir = File(System.getProperty("user.dir"))
        var clientJar = File("")
        var launcherJar = File("")
        curDir.listFiles().iterator().forEach {
            if(it.name.contains(".jar") && !it.name.contains("P3achB0t_Launcher")){
                clientJar = it
            }
            else if(it.name.contains(".jar") && it.name.contains("P3achB0t_Launcher")){
                launcherJar = it
            }
        }
        val launcherBat = File("start.bat")
        if(!launcherBat.exists()){
            launcherBat.createNewFile()
            launcherBat.writeText("java -jar \"${launcherJar.absoluteFile}\" -key $validationKey")
        }

        //TODO update client jar
        //

        curDir.listFiles().iterator().forEach {

            if(it.name.endsWith(".json")){
                println("Parsing: ${it.absoluteFile}")
                val ins: InputStream = it.inputStream()
                val content = ins.readBytes().toString(Charset.defaultCharset())
                val gson = Gson()
                val accounts: MutableList<Account> = gson.fromJson(content, object : TypeToken<List<Account>>() {}.type)
                accounts.forEach {
                    println(it)
                    //Create the individual app json file
                    val accountUser = it.username.split("@").first()
                    val accountDir = File(accountUser)
                    if(!accountDir.exists()){
                        accountDir.mkdir()
                    }
                    val appDir = File("$accountUser/app")
                    if(!appDir.exists()){
                        appDir.mkdir()
                    }
                    val usrDir = File("$accountUser/app/user")
                    if(!usrDir.exists()){
                        usrDir.mkdir()
                    }
                    val singleAccountJson = File("$accountUser/app/user/accounts.json")
                    if(!singleAccountJson.exists())
                        singleAccountJson.createNewFile()
                    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                    val jsonAccountPretty = gsonPretty.toJson(arrayOf(it))
                    singleAccountJson.writeText(jsonAccountPretty)

                    //Create batch file for easy launching individual accounts
                    val command = "java -jar \"${clientJar.absoluteFile}\" -key $validationKey"
                    val batchFile = File("$accountUser/start.bat")
                    println(batchFile)
                    if(!batchFile.exists()){
                        batchFile.createNewFile()
                    }
                    batchFile.writeText(command)

                    //Launch the JAR
                    val path = Paths.get("").toAbsolutePath().toString()
                    println("Curr dir: $path")
                    println(command)
                    val thread = Thread{
                        command.runCommand(File(accountUser))
                    }
                    thread.start()
                }
            }

        }


    }
}