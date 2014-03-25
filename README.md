# svntask #

This is a fork of [http://code.google.com/p/svntask/](http://code.google.com/p/svntask/)
and also takes some extra commands from [https://github.com/chripo/svntask/ ](https://github.com/chripo/svntask/ )

It is a simple wrapper around [svnkit](http://svnkit.com/)

Development on the original project has ended so this was created to provide a way of working with svn 1.7 via ant until [SvnAnt](http://subclipse.tigris.org/svnant.html) is updated.

Version 1.1.0

Changelog
=========
### 1.1.0 ###

- Added Export task
- Bundled with svnkit-1.7.12

### 1.0.9 ###
- bundled with svnkit-1.7.5-v1
- update Add command with fileset support (from [https://github.com/chripo/svntask/](https://github.com/chripo/svntask/))
- add svntask Auth support                (from [https://github.com/chripo/svntask/](https://github.com/chripo/svntask/))
- added new commands                      (from [https://github.com/chripo/svntask/](https://github.com/chripo/svntask/))

Available commands
==================
- Add
- Checkout
- Commit
- Export
- Info
- Log
- Ls
- Status
- Switch
- Update
- Cleanup
- Copy
- Delete
- Mkdir
- Unlock

Install
=======
Unzip the dist to a directory in your project.
Unfortunately svnkit 1.7 comes with extra dependencies so it is no longer just one jar.



TaskDef
--------------
      <path id="svnant.classpth">
        <fileset dir="lib/build/svnant">
          <include name="*.jar"/>
        </fileset>
      </path>
      <taskdef name="svn" classname="com.googlecode.svntask.SvnTask" classpathref="svnant.classpth"/>


Authentication
--------------
    <svn username="user"  password="password" >
      ... commands ...
    </svn>


Commands
------------

# cleanup
      <svn>
        <cleanup path="${project.src}" deleteWCProperties="false"/>
      </svn>


# copy
      <svn>
        <copy
          failOnDstExists="true"
          move="false"
          src="${from.file / from.url}"
          dst="${to.file / to.url}"
          commitMessage="copy by svntask"
        />
      </svn>


# delete
      <svn>
        <delete
          force="true"
          deleteFiles="false"
          includeDirs="true"
          dryRun="false"
          path="/path/to/delete"
        />
    
        <delete
          force="true"
          deleteFiles="false"
          includeDirs="true"
          dryRun="false">
          <fileset>
            ...
          </fileset>
        </delete>
      </svn>


# export
      <target name="export">
        <svn>
          <export workingcopy="/path/to/workingcopy"
                  exportpath="/path/to/exportdir"/>
        </svn>
        <svn username="guest" password="">
          <export url="http://host/svn/repo"
                  exportpath="/path/to/exportdir"/>
        </svn>
      </target>


# mkdir
      <svn>
        <mkdir
          makeParents="true"
          path="path/to/create"
          commitMessage="mkdir by svntask"
        />
      </svn>


# unlock
      <svn>
        <unlock
          breakLock="true"
          includeDirs="true">
            <fileset>
              ...
            </fileset>
        </unlock>
      </svn>
