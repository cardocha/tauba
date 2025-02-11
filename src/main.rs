mod core;

fn main() -> std::io::Result<()> {
    // Example usage for a GET request
    let request = core::curl::generate(
        "GET",
        "https://httpbin.org/json",
        &[
            ("Authorization", "Bearer token123"),
            ("Accept", "application/json"),
        ],
        None,
        "requests-testes",
        "get_command.tauba",
    )?;

    print!("output: {}", core::curl::execute(&request)?);

    Ok(())
}