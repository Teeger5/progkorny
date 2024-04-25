import {useEffect, useState} from 'react'
import './App.css'
import {AddServer} from "./components/AddServer/AddServer.tsx";
import {ServerList} from "./components/ServerList/ServerList.tsx";
import {getServers, getVersions, ServerData} from "./Utils.tsx";

/*
const versions_ = new Map<string, string>(Object.entries({
	1_20_4: "1.20.4",
	1_19_4: "1.19.4",
	1_19_3: "1.19.3",
	1_19_2: "1.19.2"
}));*/
export let VERSION_NAMES = new Array<string>()
// verzió -> mennyi szerver tartozik hozzá
export let VERSIONS = new Map<string, number>();

export let SERVERS = new Array<ServerData>();

export function setServers(servers : Array<ServerData>) {
	SERVERS = servers;
}

export function setVersions(versions : Map<string, number>) {
	VERSIONS = versions;
	VERSION_NAMES = Array.from(versions.keys());
}

export default function App() {
	const [versionNames, setVersionNames] = useState(new Array<string>());
	const [serverList, setServerList] = useState(Array<ServerData>);
	const onVersionsReceived = (json : Map<string, number>) => {
		console.log("version names: " + JSON.stringify(json.keys()));
		setVersions(json);
		setVersionNames(VERSION_NAMES);
	}
	const onServersReceived = (array : Array<ServerData>) => {
		setServers(array);
		setServerList(SERVERS);
	}
	const updateServersVersions = () => {
		getServers(onServersReceived);
		getVersions(onVersionsReceived);
	}
	useEffect(() => {
		getServers(onServersReceived);
		getVersions(onVersionsReceived);
	}, []);
	document.documentElement.lang = "hu";
	return (
		<>
			<AddServer onSuccess={updateServersVersions} />
			<ServerList/>
		</>
	)
}
