use std::fs::{self, File};
use std::io::Write;
use std::path::Path;
use std::process::Command;
use std::io::{self};
use std::os::unix::fs::PermissionsExt;
/// Generates a `curl` command for a given HTTP method, URL, headers, and optional body.
/// Saves the command to a specified folder with a specified file name.
///
/// # Arguments
/// * `method` - The HTTP method (e.g., "GET", "POST", "PUT", "DELETE").
/// * `url` - The URL for the request.
/// * `headers` - A slice of tuples representing the headers (key, value).
/// * `body` - An optional string representing the request body (for POST/PUT).
/// * `folder` - The folder where the `curl` command file will be saved.
/// * `file_name` - The name of the file to save the `curl` command.
///
/// # Returns
/// A `Result` indicating success or failure.
pub fn generate(
    method: &str,
    url: &str,
    headers: &[(&str, &str)],
    body: Option<&str>,
    folder: &str,
    file_name: &str,
) -> std::io::Result<String> {
    // Construct the headers part of the curl command
    let headers_string: String = headers
        .iter()
        .map(|(key, value)| format!("-H '{}: {}'", key, value))
        .collect::<Vec<String>>()
        .join(" ");

    // Construct the curl command based on the HTTP method
    let curl_command = match method.to_uppercase().as_str() {
        "GET" => format!("curl -X GET {} '{}'", headers_string, url),
        "POST" => {
            if let Some(body_data) = body {
                format!("curl -X POST {} -d '{}' '{}'", headers_string, body_data, url)
            } else {
                format!("curl -X POST {} '{}'", headers_string, url)
            }
        }
        "PUT" => {
            if let Some(body_data) = body {
                format!("curl -X PUT {} -d '{}' '{}'", headers_string, body_data, url)
            } else {
                format!("curl -X PUT {} '{}'", headers_string, url)
            }
        }
        "DELETE" => format!("curl -X DELETE {} '{}'", headers_string, url),
        _ => return Err(std::io::Error::new(
            std::io::ErrorKind::InvalidInput,
            "Unsupported HTTP method",
        )),
    };

    // Create the folder if it doesn't exist
    if !Path::new(folder).exists() {
        fs::create_dir_all(folder)?;
    }

    // Construct the full file path
    let file_path = Path::new(folder).join(file_name);

    // Save the curl command to the specified file
    let mut file = File::create(&file_path)?;
    file.write_all(curl_command.as_bytes())?;

    let mut permissions = fs::metadata(&file_path)?.permissions();
    permissions.set_mode(0o755); // rwxr-xr-x
    fs::set_permissions(&file_path, permissions)?;

    println!("Curl command saved to: {}", file_path.display());
    Ok(file_path.to_string_lossy().into_owned())
}

pub fn execute(curl_command: &str) -> io::Result<String> {
    // Execute the command using `sh -c`
    let output = Command::new("sh")
        .arg("-c")
        .arg(curl_command) // Pass the full command string
        .output()?; // Execute the command and capture the output

    println!("out {:?}",output);
    // Check if the command executed successfully
    if output.status.success() {
        // Convert the output bytes to a String
        let stdout = String::from_utf8(output.stdout).map_err(|e| {
            io::Error::new(io::ErrorKind::InvalidData, e)
        })?;
        print!("output: {}",stdout);
        Ok(stdout)
    } else {
        // If the command failed, return the error message from stderr
        let stderr = String::from_utf8(output.stderr).map_err(|e| {
            io::Error::new(io::ErrorKind::InvalidData, e)
        })?;
        Err(io::Error::new(io::ErrorKind::Other, stderr))
    }
}