﻿using UnityEngine;
using System;
using System.Collections;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;
using Entity = GameMachine.Messages.Entity;
using Player = GameMachine.Messages.Player;
using GameMachine.Models.Team;
using ProtoBuf;
using ProtoBuf.Meta;
using System.Reflection;


namespace GameMachine.Example
{

	public class HelloGameMachine : MonoBehaviour, GameMachineApp
	{

		public string
			foo;

		void Start ()
		{
			Login.SetGameMachineApp (this);
		}

		public void OnLoginFailure (string error)
		{
			Logger.Debug ("Authentication Failed: " + error);
		}

		public void ConnectionEstablished ()
		{
			Logger.Debug ("Connection established");
		}

		public void ConnectionTimeout ()
		{
			Logger.Debug ("Connection timed out");
		}


		// This is called once we have a connection and everything is started
		public void OnLoggedIn ()
		{
			// Start our chat example
			StartChat ();

			// Start sending/receiving location updates
			StartAreaOfInterest ();
			Logger.Debug ("AreaOfInterest started");
			//Login.regionClient.Connect ("zone", "192.168.1.8");

		}

		void StartAreaOfInterest ()
		{
			GameObject misc = GameObject.Find ("Game");
			misc.AddComponent ("AreaOfInterest");
		}

		void StartChat ()
		{
			GameObject camera = GameObject.Find ("Camera");
			GameObject chatBox = new GameObject ("ChatBox");
			chatBox.transform.parent = camera.transform;
			chatBox.AddComponent ("Chat");

			// Add Teams
			chatBox.AddComponent<TeamUi> ();
		}

	}
}
