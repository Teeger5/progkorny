import {useEffect, useState} from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {j} from "vite/dist/node/types.d-aGj9QkWt";

const host = "http://localhost:8080";

function get(route : string) {
	return fetch(host + route)
}

function post(route : string, data : Object) {
	return fetch(host + route, {
		method: "POST",
		body: data
	})
}

const versions_ = new Map<string, string>(Object.entries({
	1_20_4: "1.20.4",
	1_19_4: "1.19.4",
	1_19_3: "1.19.3",
	1_19_2: "1.19.2"
}));
let VERSIONS = new Array<string>();

function Tag({  onClick, text, selected }) {
//	const [selected, setSelected] = useState(false);
	console.log(`Tag: ${text} -> ${selected}`);
	return (
		<div onClick={onClick} style={{
			backgroundColor: selected == "1" ? "#4a4" : "transparent"
		}}>{text}</div>
	)
}
function Versions() {
	const [selectedTags, setSelectedTags] = useState(new Array<string>());
	const onTagClicked = (value: string) => {
		setSelectedTags(selectedTags.includes(value)
			? selectedTags.filter((x) => x !== value)
			: [...selectedTags, value]);
	}
	return (
		<div style={{ display: "flex" }}>
			{VERSIONS.map((k) => (
				<Tag
					key={k}
					onClick={() => onTagClicked(k)}
					text={k}
					selected={selectedTags.includes(k) ? "1" : "0"}
				/>
			))}
		</div>
	);
}

function AddServer() {
	const [version, setVersion] = useState("");
	const [name, setName] = useState("");
	const [description, setDescription] = useState("");
	const [address, setAddress] = useState("");
	const [port, setPort] = useState(25565);
	const [maxPlayers, setMaxPlayers] = useState(20);
	/*	const onVersionChanged = (value : string) => {
			version = value;
		}*/
	return (
		<div>
			<form>
				<table>
					<tbody>
					<tr>
						<td><label>Név:</label></td>
						<td><input
							type="text"
							onChange={(event) => setName(event.target.value)}/></td>
					</tr>
					<tr>
						<td>Max játékosszám:<label></label></td>
						<td><input
							type="number"
							onChange={(event) => setMaxPlayers(event.target.value)}/></td>
					</tr>
					<tr>
						<td><label>Verzió:</label></td>
						<td><select onChange={(event) => setVersion(event.target.value)}>
							{VERSIONS.map((k) => (
								<option key={k} value={k}>{k}</option>
							))}
						</select></td>
					</tr>
					<tr>
						<td>Elérés:<label></label></td>
						<td><input
							type="text"
							onChange={(event) => setAddress(event.target.value)}/>
							:<input
								type="number" style={{
								fontFamily: "monospace"
							}}
								defaultValue="25565"
								onChange={(event) => setPort(parseInt(event.target.value))}/></td>
					</tr>
					<tr>
						<td>Verziók:</td>
						<td><Versions /></td>
					</tr>
					</tbody>
				</table>
				<h3>Leírás:</h3>
				<textarea
					onChange={(event) => setDescription(event.target.value)}>
				</textarea>
				<div
					style={{
						backgroundColor: "#4a4",
						cursor: "pointer"
					}}
					onClick={() => {
						let data = {
							name: name,
							address: address,
							port: port,
							version: version,
							max_players: maxPlayers,
							description: description
						};
						console.log("new server data: " + JSON.stringify(data));
					}}>Hozzáadás</div>
			</form>
		</div>
	)
}

export default function App() {
	const [count, setCount] = useState(0)
//	const [versions, setVersions] = useState(Array<string>);

	const [versions, setVersions] = useState(new Array<string>());
	const onVersionsReceived = (json : Array<string>) => {
		VERSIONS = json;
		setVersions(VERSIONS);
	}
	useEffect(() => {
		get("/versions")
			.then((resp) => {
				return resp.json()
			})
			.then((json) => {
				onVersionsReceived(json);
				console.log("available versions: " + JSON.stringify(VERSIONS));
			});
	}, []);
	return (
		<>
			<AddServer />
			<div>
				<a href="https://vitejs.dev" target="_blank">
					<img src={viteLogo} className="logo" alt="Vite logo"/>
				</a>
				<a href="https://react.dev" target="_blank">
					<img src={reactLogo} className="logo react" alt="React logo" />
				</a>
			</div>
			<h1>Vite + React</h1>
			<div className="card">
				<button onClick={() => setCount((count) => count + 1)}>
					count is {count}
				</button>
				<p>
					Edit <code>src/App.tsx</code> and save to test HMR
				</p>
			</div>
			<p className="read-the-docs">
				Click on the Vite and React logos to learn more
			</p>
		</>
	)
}
