import {MouseEventHandler, useEffect, useState} from 'react'
import './App.css'
import Icon from "@mdi/react";
import {mdiClipboardTextMultiple} from "@mdi/js";

const host = "http://localhost:8080";

function get(route : string) {
	return fetch(host + route)
}

function post(route : string, data : Object) {
	return fetch(host + route, {
		method: "POST",
		body: JSON.stringify(data),
		headers: {
			"Content-Type": "application/json"
		}
	})
}

function getServersFiltered(onSuccess : (x : Array<ServerData>) => void, filters : ServerFilters) {
	let url = "/servers";
	if (filters !== null) {
//		url += "?filter=" + encodeURIComponent(JSON.stringify(filters));
		const params = new URLSearchParams();
		if (filters.maxPlayersMax > 0) {
			params.append("maxPlayersMax", filters.maxPlayersMax.toString());
		}
		if (filters.partOfName.length > 0) {
			params.append("partOfName", filters.partOfName);
		}
		filters.versions.forEach(v => {
			console.log("v:" + v);
			params.append("v", v);
		});
		url += "?" + params.toString();
	}
	console.log("get servers URL: " + url);
	return get(url)
		.then(resp => resp.json() as Promise<Array<ServerData>>)
		.then(servers => {
			onSuccess(servers);
			console.log("available servers: " + JSON.stringify(servers, null, 4));
		})
}

function getServers(onSuccess : (x : Array<ServerData>) => void) {
	getServersFiltered(onSuccess, null);
}

interface ServerFilters {
	versions : Array<string>,
	maxPlayersMax : number,
	partOfName : string
}
let FILTERS : ServerFilters;

interface ServerData {
	name : string;
	address : string;
	port : number;
	version : string;
	maxPlayers : number;
	description : string;
}
/*
const versions_ = new Map<string, string>(Object.entries({
	1_20_4: "1.20.4",
	1_19_4: "1.19.4",
	1_19_3: "1.19.3",
	1_19_2: "1.19.2"
}));*/
let VERSIONS = new Array<string>();

let SERVERS = new Array<ServerData>();

function Tag({  onClick, text, selected }) {
//	const [selected, setSelected] = useState(false);
//	console.log(`Tag: ${text} -> ${selected}`);
	return (
		<div onClick={onClick} style={{
			backgroundColor: selected == "1" ? "#4a4" : "transparent",
			borderRadius: 12,
			borderStyle: "solid",
			borderWidth: 3,
			borderColor: "#4a4",
			padding: 8,
			paddingLeft: 16,
			paddingRight: 16,
			fontSize: 22,
			fontWeight: "bold",
		}}>{text}</div>
	)
}
function Versions({ onSelectionChange }) {
	const [selectedTags, setSelectedTags] = useState(new Array<string>());
	const onTagClicked = (value: string) => {
		console.log("tag value: " + value);
		let array = selectedTags.includes(value)
			? selectedTags.filter((x) => x !== value)
			: [...selectedTags, value];
		console.log("tags array: " + JSON.stringify(array));
		setSelectedTags(array);
		onSelectionChange(array);
	}
	return (
		<>
			<h3>Verziók</h3>
			<div style={{
				display: "flex",
				gap: 8
			}}>
				{VERSIONS.map((k) => (
					<Tag
						key={k}
						onClick={() => onTagClicked(k)}
						text={k}
						selected={selectedTags.includes(k) ? "1" : "0"}
					/>
				))}
			</div></>
	);
}

function AddServer({ onSuccess }) {
	const [data, setData] = useState<ServerData>({
		version: 'null',
		name: '',
		description: '',
		address: '',
		port: 25565,
		maxPlayers: 20
	});
	const [msg, setMsg] = useState({
		msg: "",
		color: "transparent"
	});

	const handleInputChange = (event) => {
		const { name, value } = event.target;
		console.log(`new server data change: ${name} -> ${value}`);
		setData(prevData => ({
			...prevData,
			[name]: name === 'port' || name === 'maxPlayers' ? parseInt(value) : value
		}));
		console.log("data -> " + JSON.stringify(data));
	};

	if (VERSIONS.length > 0 && data.version === 'null') {
		setData(prevData => ({
			...prevData,
			version: VERSIONS[0]
		}));
	}

	const handleSubmit = (event : MouseEvent) => {
		event.preventDefault();
		console.log("new server data: " + JSON.stringify(data));
		post("/servers", data)
			.then(async resp => {
				let text = "✅ Szerver hozzáadva 😎"; //
				let color = "rgba(64, 160, 64, 0.5)";
				if (resp.status === 200) {
					onSuccess();
				}
				else if (resp.status === 409) {
					text = `❌ Ezen a címen (${data.address}) már található regisztrált szerver 😾`;
					color = "rgba(160, 64, 64, 0.5)";
				}
				else {
					text = await resp.text();
					color = "rgba(160, 64, 64, 0.5)";
				}
				setMsg({
					msg: text,
					color: color
				});
			});
	};
	return (
		<div>
			<form style={{
				display: "flex",
				flexFlow: "column",
				justifyContent: "stretch",
				alignItems: "stretch",
				alignContent: "stretch",
				gap: 8,
				padding: 16
			}}>
				<table>
					<tbody style={{
						display: "table"
					}}>
					<tr>
						<td><label>Név:</label></td>
						<td><input
							name="name"
							type="text"
							onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td>Max játékosszám:<label></label></td>
						<td><input
							name="maxPlayers"
							type="number"
							defaultValue="20"
							onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td><label>Verzió:</label></td>
						<td><select
							name="version"
							defaultValue={VERSIONS.length == 0 ? "" : VERSIONS[0]}
							onChange={handleInputChange}>
							{VERSIONS.map(k => (
								<option key={k} value={k}>{k}</option>
							))}
						</select></td>
					</tr>
					<tr>
						<td><label>Elérés:</label></td>
						<td style={{
							display: "flex",
						}}><input
							name="address"
							type="text"
							onChange={handleInputChange}
							style={{
								flexGrow: 1
							}}/>
							:<input
								name="port"
								type="number"
								style={{
									fontFamily: "monospace",
									width: "5em"
							}}
								defaultValue="25565"
								onChange={handleInputChange}/></td>
					</tr>
					</tbody>
				</table>
				<h3>Leírás:</h3>
				<textarea
					name="description"
					rows={8}
					onChange={handleInputChange}
					style={{
						borderRadius: 8,
						padding: 4
					}}>
				</textarea>
				<div
					style={{
						backgroundColor: "#4a4",
						cursor: "pointer",
						textAlign: "center",
						fontSize: 32,
						fontWeight: "bold",
						borderRadius: 8,
						padding: 8
					}}
					onClick={handleSubmit}>Hozzáadás</div>
				<div style={{
					backgroundColor: msg.color,
					textAlign: "center",
					fontSize: 16,
					padding: 4,
				}}>{msg.msg}</div>
			</form>
		</div>
	)
}

function ServerView ({ data }) {
	const onAddressClicked = () => {
		navigator.clipboard.writeText(`${data.address}:${data.port}`);
	}
	return (
		<div className="serverView" style={{
			display: "flex",
			flexFlow: "column",
			color: "white",
			backgroundColor: "#334",
			padding: 8,
			borderRadius: 8,
			minWidth: 400,
			maxWidth: 400
		}}>
			<h2>{data.name}</h2>
			<div>{data.version}</div>
			<div
				onClick={onAddressClicked}
				style={{
					display: "flex",
					justifyContent: "center",
					padding: 8,
					fontFamily: "monospace",
					backgroundColor: "#444",
					borderRadius: 3,
					cursor: "pointer",

				}}
			><div
				style={{
					flexGrow: 1
				}}>{data.address}:{data.port}</div>
				<Icon
					path={mdiClipboardTextMultiple}
					size={1}
					style={{

					}}/></div>
			<b>Leírás</b>
			<div>{data.description}</div>
		</div>
	)
}

function ServerList() {
	const [servers, setServers] = useState(new Array<ServerData>());
	const onVersionSelectionChange = (array : Array<string>) => {
		let filter : ServerFilters = {
			versions: array,
			partOfName: "",
			maxPlayersMax: 0
		};
		getServersFiltered(array => {
			SERVERS = array;
			setServers(SERVERS);
		}, filter);
	}
	return (
		<div style={{
			display: "flex",
			flexFlow: "column"
		}}>
			<Versions onSelectionChange={onVersionSelectionChange}/>
			<div style={{
				display: "flex",
//			flexFlow: "column",
				flexWrap: "wrap",
				width: "100%",
				gap: 8
			}}>{SERVERS.map((x, i) => (<ServerView key={i} data={x}/>))}</div>
		</div>
	)
}

export default function App() {
	const [versions, setVersions] = useState(new Array<string>());
	const [servers, setServers] = useState(Array<ServerData>);
	const onVersionsReceived = (json : Array<string>) => {
		VERSIONS = json;
		setVersions(VERSIONS);
	}
	const onServersReceived = (array : Array<ServerData>) => {
		SERVERS = array;
		setServers(SERVERS);
	}
	useEffect(() => {
		get("/versions")
			.then((resp) => {
				return resp.json() as Promise<Array<string>>
			})
			.then((json) => {
				onVersionsReceived(json);
				console.log("available versions: " + JSON.stringify(VERSIONS));
			});
		getServers(onServersReceived);
	}, []);
	document.documentElement.lang = "hu";
	return (
		<>
			<AddServer onSuccess={onServersReceived} />
			<ServerList/>
		</>
	)
}
