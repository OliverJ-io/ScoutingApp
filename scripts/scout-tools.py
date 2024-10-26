import typer
import adbutils
from rich import print as rprint
from rich.console import Console

import os
import pandas as pd

app = typer.Typer()
console = Console()

adb = adbutils.AdbClient(host="127.0.0.1", port=5037)

@app.command("install")
def install_app():
    with console.status("[bold green]Installing...[/bold green]") as status:
        tasks = []
        for d in adb.device_list():
            tasks.append(d.serial)

        if tasks == []: return

        for task in tasks:
            device = adb.device(task)
            device.install("app-release.apk", nolaunch=True, silent=True)
            console.log(f"Installed {task}")
            tasks.remove(task)

@app.command("uninstall")
def uninstall_app():
    
    with console.status("[bold red]Uninstalling...[/bold red]") as status:
        tasks = []
        for d in adb.device_list():
            tasks.append(d.serial)

        if tasks == []: return

        for task in tasks:
            device = adb.device(task)
            device.uninstall("io.oliverj.scoutingapp")
            console.log(f"Removed {task}")
            tasks.remove(task)

@app.command("extract")
def extract_matches():
    
    with console.status("[bold green]Extracting...[/bold green]") as status:
        tasks = []
        for d in adb.device_list():
            tasks.append(d.serial)

        if tasks == []: return

        for task in tasks:
            device = adb.device(task)
            device.sync.pull_dir("/storage/self/primary/Download", f"MatchFiles/{d.serial}")
            console.log(f"Extracted {task}")
            tasks.remove(task)

@app.command("compile")
def compile_data():
    with console.status("[bold green]Compiling...[/bold green]") as status:
        tasks: list[str] = []
        for devfolder in os.listdir("MatchFiles"):
            for csv in os.listdir(f"MatchFiles/{devfolder}"):
                if not csv.endswith(".csv"): continue
                tasks.append(f"MatchFiles/{devfolder}/{csv}")

        df_list = []

        for csv in tasks:
            try:
                df = pd.read_csv(csv)
                df_list.append(df)
            except UnicodeDecodeError:
                try:
                    df = pd.read_csv(csv, sep=',', encoding='utf-16')
                    df_list.append(df)
                except Exception as e:
                    print(f"Could not read file {csv} because of error: {e}")
            except Exception as e:
                print(f"Could not read file {csv} because of error: {e}")

            fname = csv.split("/")[-1]
            console.log(f"Compiled... {fname}")
            

        big_df = pd.concat(df_list, ignore_index=True)
        big_df.to_csv(f"MatchFiles/combined.csv", index=False)


if __name__ == "__main__":
    app()