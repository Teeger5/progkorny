import {useEffect, useState} from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {j, l} from "vite/dist/node/types.d-aGj9QkWt";

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

const versions_ = new Map<string, string>(Object.entries({
	1_20_4: "1.20.4",
	1_19_4: "1.19.4",
	1_19_3: "1.19.3",
	1_19_2: "1.19.2"
}));
let VERSIONS = new Array<string>();

function Tag({  onClick, text, selected }) {
//	const [selected, setSelected] = useState(false);
//	console.log(`Tag: ${text} -> ${selected}`);
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
	const [data, setData] = useState({
		version: '',
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

	if (VERSIONS.length > 0 && data.version === "") {
		setData(prevData => ({
			...prevData,
			version: VERSIONS[0]
		}));
	}

	const handleSubmit = (event) => {
		event.preventDefault();
		console.log("new server data: " + JSON.stringify(data));
		post("/servers", data)
			.then(async resp => {
				let text = "Szerver hozzáadva 😎"; //
				let color = "rgba(64, 160, 64, 0.5)";
				if (resp.status === 409) {
					text = `MEzen a címen (${data.address}) már található regisztrált szerver`;
					color = "rgba(160, 64, 64, 0.5)";
				}
				else if (resp.status !== 200) {
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
				gap: 4
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
							{VERSIONS.map((k, i) => (
								<option key={k} value={k}>{k}</option>
							))}
						</select></td>
					</tr>
					<tr>
						<td><label>Elérés:</label></td>
						<td><input
							name="address"
							type="text"
							onChange={handleInputChange}/>
							:<input
								name="port"
								type="number" style={{
								fontFamily: "monospace"
							}}
								defaultValue="25565"
								onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td>Verziók:</td>
						<td><Versions /></td>
					</tr>
					</tbody>
				</table>
				<h3>Leírás:</h3>
				<textarea
					name="description"
					rows="8"
					onChange={handleInputChange}>
				</textarea>
				<div
					style={{
						backgroundColor: "#4a4",
						cursor: "pointer"
					}}
					onClick={handleSubmit}>Hozzáadás</div>
				<div style={{
					backgroundColor: msg.color
				}}>{msg.msg}</div>
			</form>
		</div>
	)
}

export default function App() {
	const [count, setCount] = useState(0)

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
